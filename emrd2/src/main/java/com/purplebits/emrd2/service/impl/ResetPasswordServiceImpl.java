package com.purplebits.emrd2.service.impl;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.request_response.ResetPasswordResponseDTO;
import com.purplebits.emrd2.entity.OtpData;
import com.purplebits.emrd2.entity.Users;
import com.purplebits.emrd2.exceptions.EmailSendingException;
import com.purplebits.emrd2.exceptions.ExpiredOtpException;
import com.purplebits.emrd2.exceptions.InvalidEmailOrOtpExpiredException;
import com.purplebits.emrd2.exceptions.InvalidOtpException;
import com.purplebits.emrd2.exceptions.MaxOtpAttemptsExceededException;
import com.purplebits.emrd2.exceptions.OtpResendCooldownException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.repositories.AppUserRepository;
import com.purplebits.emrd2.service.EmailService;
import com.purplebits.emrd2.service.ResetPasswordService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.EmailUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

	private final Logger logger = LogManager.getLogger(ResetPasswordServiceImpl.class);
	private final String className = ResetPasswordServiceImpl.class.getSimpleName();

	@Autowired
	private AppUserRepository usersRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Value("${otp.expiry}")
	private String otpExpiryInMillisString;
	@Value("${otp.max-attempts}")
	private int MAX_OTP_ATTEMPTS;
	@Value("${otp.expiry}")
	private int OTP_EXPIRY;
	@Value("${otp.resend-cooldown}")
	private int OTP_RESEND_COOLDOWN;
	@Value("${otp.email.subject}")
	private String otpEmailSubject;
	@Autowired
	Environment environment;
	@Autowired
	private EmailUtils emailUtils;
	private Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

	@Override
	public ResetPasswordResponseDTO sendOtp(String email) throws Exception {
		logger.info(environment.getProperty(ResponseMessages.SEND_OTP_LOG));
		logger.debug(className + " sendOtp() ");
		Users user = usersRepository.findByEmailAndStatus(email, Status.ACTIVE);

		ResetPasswordResponseDTO response = new ResetPasswordResponseDTO();
		if (user == null) {
			throw new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
		}

		long currentTime = System.currentTimeMillis();
		if (otpStore.containsKey(email)) {
			OtpData existingOtpData = otpStore.get(email);
			if (currentTime - existingOtpData.getCreatedAt() < OTP_RESEND_COOLDOWN) {
				throw new OtpResendCooldownException(
						environment.getProperty(ResponseMessages.OTP_RESEND_COOLDOWN_ERROR));

			}
		}
		String otp = generateOtp();
		OtpData otpData = new OtpData(otp, currentTime, currentTime + OTP_EXPIRY);
		otpStore.put(email, otpData);
		String subject = otpEmailSubject;
		String emailBody = emailUtils.generateOtpEmailBody(user.getFullName(), email, otp, otpExpiryInMillisString);
		try {
			emailService.sendEmail(email, subject, emailBody);
		} catch (Exception e) {
			logger.error(className + " getAllSharedFilesByUserId(): " + e);
			throw new EmailSendingException(environment.getProperty(ResponseMessages.ERROR_EMAIL_SENDING));
		}

		return response;
	}

	@Override
	public ResetPasswordResponseDTO verifyOtp(String email, String otp) {
		logger.info(environment.getProperty(ResponseMessages.VERIFY_OTP_LOG));
		logger.debug(className + " verifyOtp() ");
		ResetPasswordResponseDTO response = new ResetPasswordResponseDTO();

		if (!otpStore.containsKey(email)) {
			throw new InvalidEmailOrOtpExpiredException(
					environment.getProperty(ResponseMessages.INVALID_EMAIL_OR_OTP_EXPIRED));

		}

		OtpData otpData = otpStore.get(email);

		if (System.currentTimeMillis() > otpData.getExpiryTime()) {
			otpStore.remove(email);
			throw new ExpiredOtpException(environment.getProperty(ResponseMessages.OTP_EXPIRED));
		}

		if (otpData.getOtp().equals(otp)) {
			return response;
		} else {
			otpData.incrementAttempts();
			if (otpData.getAttempts() >= MAX_OTP_ATTEMPTS) {
				otpStore.remove(email);
				throw new MaxOtpAttemptsExceededException(
						environment.getProperty(ResponseMessages.MAX_OTP_ATTEMPTS_EXCEEDED));

			} else {
				throw new InvalidOtpException(environment.getProperty(ResponseMessages.INVALID_OTP));
			}
		}

	}

	@Override
	public ResetPasswordResponseDTO resetPassword(String email, String otp, String password) {
		logger.info(environment.getProperty(ResponseMessages.RESET_PASSWORD_LOG));
		logger.debug(className + " resetPassword() ");
		Users user = usersRepository.findByEmailAndStatus(email, Status.ACTIVE);
		ResetPasswordResponseDTO response = new ResetPasswordResponseDTO();

		if (user == null) {
			throw new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
		}

		if (!otpStore.containsKey(email)) {
			throw new InvalidOtpException(environment.getProperty(ResponseMessages.INVALID_OTP));
		}

		OtpData otpData = otpStore.get(email);

		if (!otpData.getOtp().equals(otp) || System.currentTimeMillis() > otpData.getExpiryTime()) {
			throw new ExpiredOtpException(environment.getProperty(ResponseMessages.OTP_EXPIRED));
		}
		user.setPassword(passwordEncoder.encode(password));
		usersRepository.saveAndFlush(user);
		otpStore.remove(email);

		return response;
	}

	private String generateOtp() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(999999));
	}
}

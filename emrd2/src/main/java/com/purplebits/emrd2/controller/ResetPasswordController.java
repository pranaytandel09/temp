package com.purplebits.emrd2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.ResetPasswordRequestDTO;
import com.purplebits.emrd2.dto.request_response.ResetPasswordResponseDTO;
import com.purplebits.emrd2.exceptions.EmailSendingException;
import com.purplebits.emrd2.exceptions.ExpiredOtpException;
import com.purplebits.emrd2.exceptions.InvalidEmailOrOtpExpiredException;
import com.purplebits.emrd2.exceptions.InvalidOtpException;
import com.purplebits.emrd2.exceptions.MaxOtpAttemptsExceededException;
import com.purplebits.emrd2.exceptions.OtpNotFoundException;
import com.purplebits.emrd2.exceptions.OtpResendCooldownException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.service.ResetPasswordService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {
	private final String className = ResetPasswordController.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(ResetPasswordController.class);

	@Autowired
	Environment environment;
	@Autowired
	private ResetPasswordService resetPasswordService;

	@PostMapping("/sendOtp")
	public ResponseEntity<Response<ResetPasswordResponseDTO>> sendOtp(
			@RequestBody Request<ResetPasswordRequestDTO> request) throws Exception {
		Response<ResetPasswordResponseDTO> response = new Response<>();
		try {
			String email = request.getQuery().getEmail();
			ResetPasswordResponseDTO responseBody = resetPasswordService.sendOtp(email);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.OTP_SENT_SUCCESS));
			response.setResult(responseBody);
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (UserNotFoundException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " sendOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
		catch (OtpResendCooldownException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " sendOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}catch (EmailSendingException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " sendOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/verifyOtp")
	public ResponseEntity<Response<ResetPasswordResponseDTO>> verifyOtp(
			@RequestBody Request<ResetPasswordRequestDTO> request) {
		Response<ResetPasswordResponseDTO> response = new Response<>();

		try {
			String email = request.getQuery().getEmail();
			String otp = request.getQuery().getOtp();

			ResetPasswordResponseDTO responseBody = resetPasswordService.verifyOtp(email, otp);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.OTP_VERIFIED_SUCCESS));
			response.setResult(responseBody);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (InvalidEmailOrOtpExpiredException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " verifyOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
		catch (UserNotFoundException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " verifyOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
		catch ( OtpNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " verifyOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
		catch (ExpiredOtpException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " verifyOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
		catch (MaxOtpAttemptsExceededException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " verifyOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
		catch (InvalidOtpException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " verifyOtp() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<Response<ResetPasswordResponseDTO>> resetPassword(
			@RequestBody Request<ResetPasswordRequestDTO> request) {
		Response<ResetPasswordResponseDTO> response = new Response<>();

		try {
			String email = request.getQuery().getEmail();
			String otp = request.getQuery().getOtp();
			String password = request.getQuery().getNewPassword();

			ResetPasswordResponseDTO responseBody = resetPasswordService.resetPassword(email, otp, password);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.PASSWORD_RESET_SUCCESS));

			response.setResult(responseBody);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (UserNotFoundException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " resetPassword() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		catch (OtpNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " resetPassword() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		catch (ExpiredOtpException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " resetPassword() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		catch (InvalidOtpException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " resetPassword() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

}

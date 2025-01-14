package com.purplebits.emrd2.service.impl;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.exceptions.EmailSendingException;
import com.purplebits.emrd2.service.EmailService;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class EmailServiceImpl implements EmailService {
	private final Logger logger = LogManager.getLogger(EmailServiceImpl.class);
	private final String className = EmailServiceImpl.class.getSimpleName();

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	Environment environment;

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public void sendEmail(String toEmail, String subject, String body) throws Exception {
		logger.info(environment.getProperty(ResponseMessages.SEND_EMAIL_LOG));
		logger.debug(className + " sendEmail()  ");

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true);

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			logger.error(className + "sendEmail()" + e);

//        	throw new Exception(e);
			throw new EmailSendingException(environment.getProperty(ResponseMessages.ERROR_EMAIL_SENDING));

		}
	}
}

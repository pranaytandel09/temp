package com.purplebits.emrd2.service;

public interface EmailService {
	void sendEmail(String toEmail, String subject, String body) throws Exception;
}

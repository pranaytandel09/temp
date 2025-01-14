

package com.purplebits.emrd2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;



@Component
public class EmailUtils {

    @Autowired
    private Environment environment;

    @Value("${loginLink}")
    private String loginLink;

    @Value("${createPasswordLink}")
    private String createPasswordLink;

    public String generateWelcomeEmailBody(String fullName, String email) {
        String welcomeMessage = environment.getProperty(ResponseMessages.WELCOME_MESSAGE);
        String createPasswordMessage = environment.getProperty(ResponseMessages.CREATE_PASSWORD_LINK_MESSAGE);
        String loginLinkMessage = environment.getProperty(ResponseMessages.LOGIN_LINK_MESSAGE);
        String registeredEmailMessage = environment.getProperty(ResponseMessages.REGISTERED_EMAIL_MESSAGE);
        String thankYouMessage = environment.getProperty(ResponseMessages.THANK_YOU_MESSAGE);
        String greetingMessage = environment.getProperty(ResponseMessages.EMAIL_GREETING_MESSAGE);

        
        StringBuilder body = new StringBuilder();
        
        body.append("<html>")
            .append("<body>")
            .append("<p>").append(greetingMessage).append(" ").append(fullName).append(",</p>")
            .append("<p>").append(welcomeMessage).append("</p>")
            .append("<p>").append(createPasswordMessage).append("</p>")
            .append("<p><a href='").append(createPasswordLink).append("'>").append(createPasswordLink).append("</a></p>")
            .append("<p>").append(loginLinkMessage).append("</p>")
            .append("<p><a href='").append(loginLink).append("'>").append(loginLink).append("</a></p>")
            .append("<p>").append(registeredEmailMessage).append(": <strong>").append(email).append("</strong></p>")
            .append("<p>").append(thankYouMessage).append("</p>")
            .append("</body>")
            .append("</html>");

        return body.toString();
    }

    public String generateOtpEmailBody(String fullName, String email, String otp, String otpExpiryInMillisString) {
        String otpMessage = environment.getProperty(ResponseMessages.OTP_MESSAGE);
        String otpExpiryMessage = environment.getProperty(ResponseMessages.OTP_EXPIRY_MESSAGE);
        String otpWarningMessage = environment.getProperty(ResponseMessages.OTP_WARNING_MESSAGE);
        String otpProceedMessage = environment.getProperty(ResponseMessages.OTP_PROCEED_MESSAGE); // New dynamic message
        String thankYouMsg=environment.getProperty(ResponseMessages.THANK_YOU_MESSAGE);
        String greetingMessage = environment.getProperty(ResponseMessages.EMAIL_GREETING_MESSAGE);

        StringBuilder body = new StringBuilder();
        String trimmedExpiryString = otpExpiryInMillisString.trim();
        long expiryInMinutes = Long.parseLong(trimmedExpiryString) / 60000;

        body.append("<html>")
            .append("<body>")
            .append("<p>").append(greetingMessage).append(" ").append(fullName).append(",</p>")
            .append("<p>").append(otpMessage).append(" <strong>").append(otp).append("</strong></p>")
            .append("<p>").append(otpProceedMessage).append("</p>")
            .append("<p>").append(String.format(otpExpiryMessage, expiryInMinutes)).append("</p>")
            .append("<p>").append(otpWarningMessage).append("</p>")
            .append("<p>").append(thankYouMsg).append("</p>")
            .append("</body>")
            .append("</html>");

        return body.toString();
    }
}


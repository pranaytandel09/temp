package com.purplebits.emrd2.exceptions;

public class OtpResendCooldownException extends RuntimeException {
    public OtpResendCooldownException(String message) {
        super(message);
    }
}

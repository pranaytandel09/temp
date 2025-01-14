package com.purplebits.emrd2.exceptions;

public class MaxOtpAttemptsExceededException extends RuntimeException {
	public MaxOtpAttemptsExceededException(String message) {
        super(message);
    }
}

package com.purplebits.emrd2.exceptions;

public class InvalidEmailOrOtpExpiredException extends RuntimeException {
	public InvalidEmailOrOtpExpiredException(String str) {
		super(str);
	}

}

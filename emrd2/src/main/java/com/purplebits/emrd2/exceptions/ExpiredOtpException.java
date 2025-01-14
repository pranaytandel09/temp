package com.purplebits.emrd2.exceptions;

public class ExpiredOtpException extends RuntimeException {
	public ExpiredOtpException(String str) {
		super(str);
	}

}

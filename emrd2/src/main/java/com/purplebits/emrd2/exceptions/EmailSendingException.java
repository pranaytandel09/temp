package com.purplebits.emrd2.exceptions;

public class EmailSendingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailSendingException(String message) {
		super(message);
	}
}

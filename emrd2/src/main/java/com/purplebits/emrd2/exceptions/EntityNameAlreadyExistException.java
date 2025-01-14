package com.purplebits.emrd2.exceptions;


public class EntityNameAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNameAlreadyExistException(String message) {
		super(message);
	}
}

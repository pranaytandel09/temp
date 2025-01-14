package com.purplebits.emrd2.exceptions;


public class ProjectNameAlreadyExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ProjectNameAlreadyExistException(String message) {
		super(message);
	}
}

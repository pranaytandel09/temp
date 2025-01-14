package com.purplebits.emrd2.exceptions;

public class TaskStatusNotValidException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TaskStatusNotValidException(String message) {
		super(message);
	}
}

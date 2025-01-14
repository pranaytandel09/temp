package com.purplebits.emrd2.exceptions;

public class WorkflowNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public WorkflowNotFoundException(String message) {
		super(message);
	}
}

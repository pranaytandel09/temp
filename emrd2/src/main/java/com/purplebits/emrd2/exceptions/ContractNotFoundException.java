package com.purplebits.emrd2.exceptions;

public class ContractNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ContractNotFoundException(String message) {
		super(message);
	}
}

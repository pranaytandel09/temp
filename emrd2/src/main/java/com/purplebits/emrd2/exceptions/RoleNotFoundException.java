package com.purplebits.emrd2.exceptions;



public class RoleNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleNotFoundException(String message) {
        super(message);
    }
}

package com.purplebits.emrd2.exceptions;

public class PermissionNotFoundException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermissionNotFoundException(String msg) {
        super(msg);
    }
}

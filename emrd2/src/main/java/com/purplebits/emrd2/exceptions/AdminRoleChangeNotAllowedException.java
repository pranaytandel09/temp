package com.purplebits.emrd2.exceptions;


public class AdminRoleChangeNotAllowedException extends RuntimeException {
    public AdminRoleChangeNotAllowedException(String message) {
        super(message);
    }
}

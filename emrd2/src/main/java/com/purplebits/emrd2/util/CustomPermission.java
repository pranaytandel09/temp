package com.purplebits.emrd2.util;

import org.springframework.security.acls.model.Permission;

public class CustomPermission implements Permission{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int mask;
    private final String name;

    public CustomPermission(int mask, String name) {
        this.mask = mask;
        this.name = name;
    }

    @Override
    public int getMask() {
        return this.mask;
    }

    @Override
    public String getPattern() {
        return Integer.toBinaryString(mask);
    }

    @Override
    public String toString() {
        return this.name + " (" + this.getMask() + ")";
    }
}

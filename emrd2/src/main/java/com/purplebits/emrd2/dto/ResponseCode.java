package com.purplebits.emrd2.dto;

public final class ResponseCode {

	// Successful
	public static final Integer OK = 200;

	// Create OK
	public static final Integer CREATED_OK = 201;

	// Request Accepted
	public static final Integer ACCEPTED = 202;

	// Delete OK
	public static final Integer DELETED_OK = 204;

	// Already Used Resource
	public static final Integer IM_USED = 226;

	// There was no new data to return.
	public static final Integer CODE_NOTMODIFIED = 304;

	// The request was invalid or cannot be otherwise served. Error message
	// should give explanation.
	public static final Integer BAD_REQUEST = 400;

	// Login credentials were missing or incorrect.
	public static final Integer UNAUTHORIZED = 401;

	// Access is not allowed.
	public static final Integer FORBIDDEN = 403;

	// Internal Server Error
	public static final Integer INTERNAL_SERVER_ERROR = 500;

	// Not Found
	public static final Integer NOT_FOUND = 404;
	// Conflict
	public static final Integer CONFLICT = 409;

	// Client Request Invalid
	public static final Integer INVALID_REQUEST = 600;
	// Expired
	public static final Integer OTP_EXPIRED = 601;

	public static final Integer LICENSE_EXPIRED = 600;
	
	public static final Integer TOO_MANY_REQUESTS = 429;
	public static final Integer INACTIVE_USER = 403;
}

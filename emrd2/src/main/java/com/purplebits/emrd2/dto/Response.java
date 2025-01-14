package com.purplebits.emrd2.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.purplebits.emrd2.util.JsonUtils;
import com.purplebits.emrd2.util.UUIDUtils;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Response<T> {

	private int code;
	private String message;
	private T result;
	private String requestId = UUIDUtils.generateUUID();
	private String timestamp = Instant.now().toString();
	private String loggedInUser;
	
	
	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

	
}

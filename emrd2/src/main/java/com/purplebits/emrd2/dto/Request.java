package com.purplebits.emrd2.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Request<T> {

	private T query;
	private String requestId;
	@JsonIgnore
	private String timestamp;
	private String loggedInUser;
	private Integer loggedInUserID;
	private String loggedInUserType;
	
	

	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public T getQuery() {
		return query;
	}

	public void setQuery(T query) {
		this.query = query;
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

	public Request() {
	}

/**
	 * @param query
	 * @param requestId
	 * @param loggedInUser
	 * @param timestamp
	 */
	public Request(T query, String requestId, String loggedInUser, String timestamp) {
		super();
		this.query = query;
		this.requestId = requestId;
		this.loggedInUser = loggedInUser;
		this.timestamp = timestamp;
	}
	
	
	

	

public Integer getLoggedInUserID() {
	return loggedInUserID;
}

public void setLoggedInUserID(Integer loggedInUserID) {
	this.loggedInUserID = loggedInUserID;
}

public String getLoggedInUserType() {
	return loggedInUserType;
}

public void setLoggedInUserType(String loggedInUserType) {
	this.loggedInUserType = loggedInUserType;
}

	@Override
	public String toString() {
		return "Request [query=" + query + ", requestId=" + requestId + ", timestamp=" + timestamp + "]";
	}

}

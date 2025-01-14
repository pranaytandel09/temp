package com.purplebits.emrd2.dto;

import com.purplebits.emrd2.util.JsonUtils;

public class PaginationWithSearchRequestDTO {

	private PaginationRequestDTO paginationRequest;
	private String search;

	public PaginationWithSearchRequestDTO() {
		super();
	}

	public PaginationRequestDTO getPaginationRequest() {
		return paginationRequest;
	}

	public void setPaginationRequest(PaginationRequestDTO paginationRequest) {
		this.paginationRequest = paginationRequest;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}

package com.purplebits.emrd2.dto;

import com.purplebits.emrd2.util.JsonUtils;

public class PaginationDTO {

	private int page;
    private int limit;
    private long totalCounts;
    private int totalPages;
    private boolean isLastPage;
    
     
    
	public PaginationDTO(int page, int limit, long totalCounts, int totalPages, boolean isLastPage) {
		super();
		this.page = page;
		this.limit = limit;
		this.totalCounts = totalCounts;
		this.totalPages = totalPages;
		this.isLastPage = isLastPage;
	}
	public PaginationDTO() {
		super();
	}
	

	public boolean isLastPage() {
		return isLastPage;
	}
	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public long getTotalCounts() {
		return totalCounts;
	}
	public void setTotalCounts(long totalCounts) {
		this.totalCounts = totalCounts;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
    
    
}

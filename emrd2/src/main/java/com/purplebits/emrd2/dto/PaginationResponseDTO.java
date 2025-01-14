package com.purplebits.emrd2.dto;

import java.util.List;



public class PaginationResponseDTO<T> {

	 private PaginationDTO pagination;
	 private List<T> data;
	public PaginationDTO getPagination() {
		return pagination;
	}
	public void setPagination(PaginationDTO pagination) {
		this.pagination = pagination;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public PaginationResponseDTO(PaginationDTO pagination, List<T> data) {
		super();
		this.pagination = pagination;
		this.data = data;
	}
	public PaginationResponseDTO() {
		super();
	}
	@Override
	public String toString() {
		return "PaginationResponseDTO [pagination=" + pagination + ", data=" + data + "]";
	}
	
	 
}

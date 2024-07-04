package com.ta.ditec.services.response;

import org.springframework.data.domain.Page;

public class PaginationMeta {

	private Long totalCount;
	private Integer pageSize;
	private Integer totalPage;
	private Integer pageNumber;
	private Boolean isLast;
	private Boolean isFirst;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}

	public static <T> PaginationMeta createPagination(Page<T> pageable) {
		PaginationMeta pagination = new PaginationMeta();
		pagination.setPageNumber(pageable.getNumber());
		pagination.setIsFirst(pageable.isFirst());
		pagination.setIsLast(pageable.isLast());
		pagination.setTotalCount(Long.valueOf(pageable.getTotalElements()));
		pagination.setTotalPage(pageable.getTotalPages());
		pagination.setPageSize(pageable.getSize());

		return pagination;
	}

}

package com.taskmanager.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class DataTablesResponse<T> {

	private int currentPage;
	private long totalItems;
	private int totalPages;
	private List<T> data;
}

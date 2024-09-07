package com.outsera.goldenraspberryawards.api.v1.openapi.model;

import lombok.Data;

import java.util.List;

@Data
public abstract class PagedBaseResponseDTO<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

}

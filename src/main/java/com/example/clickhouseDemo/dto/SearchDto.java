package com.example.clickhouseDemo.dto;

import lombok.Data;

@Data
public class SearchDto {
    private int page;
    private int dataCnt;
    private String keyword;

    public SearchDto() {
        this.page = 1;
        this.dataCnt = 10;
    }
}

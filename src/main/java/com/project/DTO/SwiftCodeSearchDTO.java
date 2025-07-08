package com.project.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwiftCodeSearchDTO {

    @JsonProperty("BANK_TYPE")
    private String BANK_TYPE;

    @JsonProperty("BANK_NAME")
    private String BANK_NAME;

    @JsonProperty("BRANCH")
    private String BRANCH;

    @JsonProperty("CITY")
    private String CITY;

    @JsonProperty("COUNTRY_CODE")
    private String COUNTRY_CODE;

    @JsonProperty("page_number")
    private int page_number = 0;

    @JsonProperty("page_size")
    private int page_size = 5;

    // Constructors
    public SwiftCodeSearchDTO() {
    }

    public SwiftCodeSearchDTO(String BANK_TYPE, String BANK_NAME, String BRANCH, String CITY, String COUNTRY_CODE,
                              int page_number, int page_size) {
        this.BANK_TYPE = BANK_TYPE;
        this.BANK_NAME = BANK_NAME;
        this.BRANCH = BRANCH;
        this.CITY = CITY;
        this.COUNTRY_CODE = COUNTRY_CODE;
        this.page_number = page_number;
        this.page_size = page_size;
    }

    // Getters and Setters
    public String getBANK_TYPE() {
        return BANK_TYPE;
    }

    public void setBANK_TYPE(String BANK_TYPE) {
        this.BANK_TYPE = BANK_TYPE;
    }

    public String getBANK_NAME() {
        return BANK_NAME;
    }

    public void setBANK_NAME(String BANK_NAME) {
        this.BANK_NAME = BANK_NAME;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String BRANCH) {
        this.BRANCH = BRANCH;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getCOUNTRY_CODE() {
        return COUNTRY_CODE;
    }

    public void setCOUNTRY_CODE(String COUNTRY_CODE) {
        this.COUNTRY_CODE = COUNTRY_CODE;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }
}
package com.lucid.recruit.org.dto;

public class OrganizationTypeCountDto {
    private String organizationType;
    private  Long count;

    public OrganizationTypeCountDto(String organizationType, Long count) {
        this.organizationType = organizationType;
        this.count = count;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

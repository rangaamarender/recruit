package com.lucid.recruit.org.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = OrgAddressType.TABLE_NAME)
public class OrgAddressType {

    public static final String TABLE_NAME="ref_org_addrs_type";

    @Id
    @Column(name = "address_type")
    private String addressType;

    @Column(name = "displayName",unique = true)
    private String displayName;

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}

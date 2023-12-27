package com.lucid.recruit.referencedata.entity;

import com.lucid.recruit.referencedata.constants.EnumParametersType;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = TenantParameters.TABLE_NAME)
public class TenantParameters {

    public static final String TABLE_NAME = "ref_tenant_parameters";

    @Id
    @Column(name = "name", nullable = false, length = 75)
    private String name;
    @Column(name="pmter_value", nullable = false, length = 100)
    private String value;
    @Enumerated(EnumType.STRING)
    @Column(name = "pmter_type",nullable = false)
    private EnumParametersType type;
    @Column(name = "date", nullable = true)
    private LocalDate date;

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getValue() {return value;}

    public void setValue(String value){this.value = value;}

    public EnumParametersType getType() {return type;}

    public void setType(EnumParametersType type) {this.type = type;}

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}
}

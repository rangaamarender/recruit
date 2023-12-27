package com.lucid.recruit.referencedata.dto;
import com.lucid.recruit.referencedata.constants.EnumParametersType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TenantParametersDTO {
    @NotNull
    private String name;
    @NotNull
    private String value;
    @NotNull
    private EnumParametersType type;
    @Nullable
    private LocalDate date;


    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getValue() {return value;}

    public void setValue(String value) {this.value = value;}

    public EnumParametersType getType() {return type;}

    public void setType(EnumParametersType type) {this.type = type;}

    public LocalDate getDate() {return date;}

    public void setDate(LocalDate date) {this.date = date;}
}
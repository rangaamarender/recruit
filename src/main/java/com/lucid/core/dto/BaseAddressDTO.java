/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.core.dto;

import com.lucid.recruit.referencedata.dto.AddressFormatDTO;
import com.lucid.recruit.referencedata.dto.CountryDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author sgutti
 * @date 16-Mar-2023 5:39:08 am
 *
 */
public class BaseAddressDTO {

	@NotNull(message ="addressName need to fill" )
	private String addressName;
	@NotNull(message ="addressName need to fill" )
	private String address1;
	@Nullable
	private String address2;
	@Nullable
	private String address3;
	@Nullable
	private String address4;
	@Nullable
	private String address5;

	@Nullable
	private String city;

	@Nullable
	private String state;

	@Nullable
	private String postalCode;
	@Nullable
	private String postOfficeBox;
	@NotNull(message ="country mandatory")
	private CountryDTO country;
	@Nullable
	@Valid
	private GeoCodeDTO geoCode;

	@Nullable
	private AddressFormatDTO addressFormat;

	public BaseAddressDTO() {
		super();
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getAddress5() {
		return address5;
	}

	public void setAddress5(String address5) {
		this.address5 = address5;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPostOfficeBox() {
		return postOfficeBox;
	}

	public void setPostOfficeBox(String postOfficeBox) {
		this.postOfficeBox = postOfficeBox;
	}

	public GeoCodeDTO getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(GeoCodeDTO geoCode) {
		this.geoCode = geoCode;
	}

	public CountryDTO getCountry() {
		return country;
	}

	public void setCountry(CountryDTO country) {
		this.country = country;
	}


	public AddressFormatDTO getAddressFormat() {
		return addressFormat;
	}

	public void setAddressFormat(AddressFormatDTO addressFormat) {
		this.addressFormat = addressFormat;
	}
}

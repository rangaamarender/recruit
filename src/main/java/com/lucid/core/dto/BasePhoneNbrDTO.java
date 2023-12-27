/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.core.dto;

import com.lucid.core.annotations.Phone;
import com.lucid.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author sgutti
 * @date 11-Feb-2023 4:57:33 pm
 *
 */
@MappedSuperclass
@Embeddable
public class BasePhoneNbrDTO extends BaseEntity {

	private static final long serialVersionUID = -6475608104948467200L;

	// The country dialing code for a communication number.
	private String countryDialingCode;

	// The area dialing code for a communication number.
	private String areaDialingCode;

	// The communication number, not including country dialing or area dialing
	// codes.
	@NotNull
	@Phone
	private String dialNumber;

	// The extension of the associated communication number.
	private String phoneExtension;

	// The text that permits access to the electronic network of the associated
	// communication number
	// such as telephone network, for example 9, *70.
	private String access;

	/**
	 * Create a new <code>BasePhoneNbr</code>
	 */
	public BasePhoneNbrDTO() {
		super();
	}

	public BasePhoneNbrDTO(String countryDialingCode, String areaDialingCode, String dialNumber, String phoneExtension,
                           String access) {
		super();
		this.countryDialingCode = countryDialingCode;
		this.areaDialingCode = areaDialingCode;
		this.dialNumber = dialNumber;
		this.phoneExtension = phoneExtension;
		this.access = access;
	}

	public BasePhoneNbrDTO(String dialNumber) {
		super();
		this.dialNumber = dialNumber;
	}

	public String getCountryDialingCode() {
		return countryDialingCode;
	}

	public void setCountryDialingCode(String countryDialingCode) {
		this.countryDialingCode = countryDialingCode;
	}

	public String getAreaDialingCode() {
		return areaDialingCode;
	}

	public void setAreaDialingCode(String areaDialingCode) {
		this.areaDialingCode = areaDialingCode;
	}

	public String getDialNumber() {
		return dialNumber;
	}

	public void setDialNumber(String dialNumber) {
		this.dialNumber = dialNumber;
	}

	public String getPhoneExtension() {
		return phoneExtension;
	}

	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

}

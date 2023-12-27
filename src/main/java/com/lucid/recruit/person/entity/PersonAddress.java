package com.lucid.recruit.person.entity;

import java.time.LocalDate;

import com.lucid.core.entity.BaseAddress;

import com.lucid.recruit.org.entity.OrgAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = PersonAddress.TABLE_NAME)
public class PersonAddress extends BaseAddress {

	private static final long serialVersionUID = -2004215414831042229L;
	public static final String TABLE_NAME = "p_prsn_addr";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "prsn_address_id", nullable = false, length = 75)
	private String personAddressID;

	@Enumerated(EnumType.STRING)
	@Column(name = "address_type", nullable = false, length = 25)
	private PersonAddressType addressType;

	@Column(name = "start_dat", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dat", nullable = true)
	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "person_legal_id", nullable = false, updatable = false)
	private PersonLegal personLegal;

	/**
	 * Create a new <code>PersonAddress</code>
	 */
	public PersonAddress() {
		super();
	}

	public String getPersonAddressID() {
		return personAddressID;
	}

	public void setPersonAddressID(String personAddressID) {
		this.personAddressID = personAddressID;
	}

	public PersonLegal getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegal personLegal) {
		this.personLegal = personLegal;
	}

	public PersonAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(PersonAddressType addressType) {
		this.addressType = addressType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		PersonAddress personAddress = (PersonAddress) obj;

		if(getAddressName() == null){
			if(personAddress.getAddressName() != null){
				return false;
			}
		}
		else if(!getAddressName().equals(personAddress.getAddressName())){
			return false;
		}
		if(getAddress1() == null){
			if(personAddress.getAddress1() != null){
				return false;
			}
		}
		else if(!getAddress1().equals(personAddress.getAddress1())){
			return false;
		}
		if(getAddress2() == null){
			if(personAddress.getAddress2() != null){
				return false;
			}
		}
		else if(!getAddress2().equals(personAddress.getAddress2())){
			return false;
		}
		if(getAddress3() == null){
			if(personAddress.getAddress3() != null){
				return false;
			}
		}
		else if(!getAddress3().equals(personAddress.getAddress3())){
			return false;
		}
		if(getAddress4() == null){
			if(personAddress.getAddress4() != null){
				return false;
			}
		}
		else if(!getAddress4().equals(personAddress.getAddress4())){
			return false;
		}
		if(getAddress5() == null){
			if(personAddress.getAddress5() != null){
				return false;
			}
		}
		else if(!getAddress5().equals(personAddress.getAddress5())){
			return false;
		}
		if(getCity() == null){
			if(personAddress.getCity() != null){
				return false;
			}
		}
		else if(!getCity().equals(personAddress.getCity())){
			return false;
		}
		if(getPostalCode() == null){
			if(personAddress.getPostalCode() != null){
				return false;
			}
		}
		else if(!getPostalCode().equals(personAddress.getPostalCode())){
			return false;
		}
		if(getPostOfficeBox() == null){
			if(personAddress.getPostOfficeBox() != null){
				return false;
			}
		}
		else if(!getPostOfficeBox().equals(personAddress.getPostOfficeBox())){
			return false;
		}
		if(getAddressType() == null){
			if(personAddress.getAddressType() != null){
				return false;
			}
		}
		else if(!getAddressType().equals(personAddress.getAddressType())){
			return false;
		}
		return true;
	}

}

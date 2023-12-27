/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.org.entity;

import java.time.LocalDate;
import java.util.Comparator;

import com.lucid.core.entity.BaseAddress;

import jakarta.persistence.*;

@Entity
@Table(name = OrgAddress.TABLE_NAME,uniqueConstraints = {@UniqueConstraint(name = "UNIQUEORGADRESS",columnNames = {"organization_id","address_type","start_date"})})
public class OrgAddress extends BaseAddress {

	// --------------------------------------------------------------- Constants
	private static final long serialVersionUID = -3047943864801805071L;
	public static final String TABLE_NAME = "o_org_addr";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "org_address_id", nullable = false, length = 75)
	private String orgAddressID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id",nullable = false, updatable = false)
	private Organization organization;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_type",nullable = false)
	private OrgAddressType orgAddressType;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = true)
	private LocalDate endDate;
	public OrgAddress() {
	}

	public String getOrgAddressID() {
		return orgAddressID;
	}

	public void setOrgAddressID(String orgAddressID) {
		this.orgAddressID = orgAddressID;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public OrgAddressType getOrgAddressType() {
		return orgAddressType;
	}

	public void setOrgAddressType(OrgAddressType orgAddressType) {
		this.orgAddressType = orgAddressType;
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

	public static Comparator<OrgAddress> createOrgAddLambdaComparator(){
		return Comparator.comparing(OrgAddress::getStartDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true; // Same object instance
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false; // Different class or null
		}

		OrgAddress orgAddress = (OrgAddress) obj;

		if(getAddressName() == null){
			if(orgAddress.getAddressName() != null){
				return false;
			}
		}
		else if(!getAddressName().equals(orgAddress.getAddressName())){
		 return false;
		}
		if(getAddress1() == null){
			if(orgAddress.getAddress1() != null){
				return false;
			}
		}
		else if(!getAddress1().equals(orgAddress.getAddress1())){
			return false;
		}
		if(getAddress2() == null){
			if(orgAddress.getAddress2() != null){
				return false;
			}
		}
		else if(!getAddress2().equals(orgAddress.getAddress2())){
			return false;
		}
		if(getAddress3() == null){
			if(orgAddress.getAddress3() != null){
				return false;
			}
		}
		else if(!getAddress3().equals(orgAddress.getAddress3())){
			return false;
		}
		if(getAddress4() == null){
			if(orgAddress.getAddress4() != null){
				return false;
			}
		}
		else if(!getAddress4().equals(orgAddress.getAddress4())){
			return false;
		}
		if(getAddress5() == null){
			if(orgAddress.getAddress5() != null){
				return false;
			}
		}
		else if(!getAddress5().equals(orgAddress.getAddress5())){
			return false;
		}
		if(getCity() == null){
			if(orgAddress.getCity() != null){
				return false;
			}
		}
		else if(!getCity().equals(orgAddress.getCity())){
			return false;
		}
		if(getCountry() == null){
			if(orgAddress.getCountry() != null){
				return false;
			}
		}
		else if(!getCountry().getCountryCode().equals(orgAddress.getCountry().getCountryCode())){
			return false;
		}
		if(getPostalCode() == null){
			if(orgAddress.getPostalCode() != null){
				return false;
			}
		}
		else if(!getPostalCode().equals(orgAddress.getPostalCode())){
			return false;
		}
		if(getPostOfficeBox() == null){
			if(orgAddress.getPostOfficeBox() != null){
				return false;
			}
		}
		else if(!getPostOfficeBox().equals(orgAddress.getPostOfficeBox())){
			return false;
		}
		return true;
	}

}

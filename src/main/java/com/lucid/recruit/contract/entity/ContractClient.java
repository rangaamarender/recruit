/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.entity;

import com.lucid.core.entity.AuditableEntity;
import com.lucid.recruit.org.entity.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.util.Comparator;

@Entity
@Table(name = ContractClient.TABLE_NAME, uniqueConstraints = @UniqueConstraint(name = "UQCONTRACTCLIENT_1", columnNames = {
		"c_wrk_order_id", "client_sequence" }))
public class ContractClient extends AuditableEntity {

	// --------------------------------------------------------------- Constants
	private static final long serialVersionUID = 2802777372903198187L;
	public static final String TABLE_NAME = "c_cntrct_client";

	// UUID of the contract client
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "c_cntrct_client_id", nullable = false, length = 75)
	private String contractClientID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_wrk_order_id", nullable = false, updatable = false)
	private ContractWorkOrder workOrder;

	// Client organization ID
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "client_id", referencedColumnName = "org_id", nullable = false)
	private Organization client;

	@Column(name = "end_client_boo",nullable = false)
	private Boolean endClientBoo;

	@Column(name = "client_sequence", nullable = false)
	private int clientSequence;

	public ContractClient() {
		super();
	}

	public String getContractClientID() {
		return contractClientID;
	}

	public void setContractClientID(String contractClientID) {
		this.contractClientID = contractClientID;
	}

	public ContractWorkOrder getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(ContractWorkOrder workOrder) {
		this.workOrder = workOrder;
	}

	public Organization getClient() {
		return client;
	}

	public void setClient(Organization client) {
		this.client = client;
	}

	public int getClientSequence() {
		return clientSequence;
	}

	public void setClientSequence(int clientSequence) {
		this.clientSequence = clientSequence;
	}

	public Boolean getEndClientBoo() {
		return endClientBoo;
	}

	public void setEndClientBoo(Boolean endClientBoo) {
		this.endClientBoo = endClientBoo;
	}

	public static Comparator<ContractClient> createContractClientLambdaComparator(){
		return Comparator.comparing(ContractClient::getClientSequence);
	}

}

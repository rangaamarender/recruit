package com.lucid.recruit.worker.service;

import java.util.Comparator;

import com.lucid.recruit.person.entity.PersonAddress;

public class PrsnAddressComparator {

	public static Comparator<PersonAddress> createPersonLambdaComparator() {
		return Comparator.comparing(PersonAddress::getAddressType).thenComparing(PersonAddress::getStartDate);
	}

}

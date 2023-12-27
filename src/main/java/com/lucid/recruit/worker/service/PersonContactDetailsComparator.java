package com.lucid.recruit.worker.service;

import java.util.Comparator;

import com.lucid.recruit.person.entity.PersonContactDetails;

public class PersonContactDetailsComparator {

	public static Comparator<PersonContactDetails> createPrsnCntctDetailsComparator() {
		return Comparator.comparing(PersonContactDetails::getContactType)
				.thenComparing(PersonContactDetails::getStartDate);
	}
}

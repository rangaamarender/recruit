package com.lucid.recruit.worker.service;

import java.util.Comparator;

import com.lucid.recruit.worker.entity.WorkAssignment;

public class WorkAssignmentComparator {

	public static Comparator<WorkAssignment> createWSComparator() {
		return Comparator.comparing(WorkAssignment::getStartDate);
	}
}

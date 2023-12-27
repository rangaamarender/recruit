package com.lucid.recruit.person.entity;

import java.util.ArrayList;
import java.util.List;

public enum RelationshipCode {
	AdoptedChild, Child, Brother, Sister, Mother, Father, Spouse, Exspouse, Grandmother, Grandfather, Grandson,
	Granddaughter;

	public static List<RelationshipCode> fetchAllRelationShipCodes(){
		List<RelationshipCode> relationshipCodeList = new ArrayList<>();
		relationshipCodeList.add(RelationshipCode.AdoptedChild);
		relationshipCodeList.add(RelationshipCode.Child);
		relationshipCodeList.add(RelationshipCode.Brother);
		relationshipCodeList.add(RelationshipCode.Sister);
		relationshipCodeList.add(RelationshipCode.Mother);
		relationshipCodeList.add(RelationshipCode.Father);
		relationshipCodeList.add(RelationshipCode.Spouse);
		relationshipCodeList.add(RelationshipCode.Exspouse);
		relationshipCodeList.add(RelationshipCode.Grandmother);
		relationshipCodeList.add(RelationshipCode.Grandfather);
		relationshipCodeList.add(RelationshipCode.Grandson);
		relationshipCodeList.add(RelationshipCode.Granddaughter);
		return relationshipCodeList;
	}

}

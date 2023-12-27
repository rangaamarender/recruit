package com.lucid.recruit.person.dto;

import com.lucid.recruit.docs.dto.BaseSignableDocumentDTO;
import com.lucid.recruit.person.entity.PersonLegalDocument;

public class PersonLeglaSignableDocDTO extends BaseSignableDocumentDTO {

    private String signDocID;

    // organization id of the party B
    private PersonLegalDocument personLegalDocument;

    public String getSignDocID() {
        return signDocID;
    }

    public void setSignDocID(String signDocID) {
        this.signDocID = signDocID;
    }

    public PersonLegalDocument getPersonLegalDocument() {
        return personLegalDocument;
    }

    public void setPersonLegalDocument(PersonLegalDocument personLegalDocument) {
        this.personLegalDocument = personLegalDocument;
    }
}

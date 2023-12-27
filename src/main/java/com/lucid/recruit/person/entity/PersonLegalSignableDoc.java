package com.lucid.recruit.person.entity;

import com.lucid.recruit.contract.entity.ContractSignableDoc;
import com.lucid.recruit.docs.entity.BaseSignableDocument;
import jakarta.persistence.*;

@Entity
@Table(name = ContractSignableDoc.TABLE_NAME)
public class PersonLegalSignableDoc extends BaseSignableDocument {

    public static final String TABLE_NAME = "person_legal_sig_doc";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sign_doc_id", nullable = false, length = 75)
    private String signDocID;

    // organization id of the party B
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "legal_doc_id", nullable = false)
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

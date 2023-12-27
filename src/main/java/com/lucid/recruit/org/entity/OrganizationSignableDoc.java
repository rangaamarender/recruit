/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.org.entity;

import com.lucid.recruit.docs.entity.BaseSignableDocument;
import com.lucid.recruit.person.entity.PersonLegalDocument;
import jakarta.persistence.*;

/**
 *
 */
@Entity
@Table(name = OrganizationSignableDoc.TABLE_NAME)
public class OrganizationSignableDoc extends BaseSignableDocument {
  // --------------------------------------------------------------- Constants
  private static final long serialVersionUID = 8439853915481350523L;
  public static final String TABLE_NAME = "o_org_sig_doc";
  // --------------------------------------------------------- Class Variables
  // ----------------------------------------------------- Static Initializers
  // ------------------------------------------------------ Instance Variables
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "sign_doc_id", nullable = false, length = 75)
  private String signDocID;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "organization_doc_id", nullable = false)
  private OrganizationDocument organizationDocument;

  // ------------------------------------------------------------ Constructors
  /**
   * Create a new <code>OrganizationSignableDoc</code>
   */
  public OrganizationSignableDoc() {
    super();
  }

  // ---------------------------------------------------------- Public Methods

  public String getSignDocID() {
    return signDocID;
  }

  public void setSignDocID(String signDocID) {
    this.signDocID = signDocID;
  }

  public OrganizationDocument getOrganizationDocument() {
    return organizationDocument;
  }

  public void setOrganizationDocument(OrganizationDocument organizationDocument) {
    this.organizationDocument = organizationDocument;
  }


  // ------------------------------------------------------- Protected Methods
  // --------------------------------------------------------- Default Methods
  // --------------------------------------------------------- Private Methods
  // ---------------------------------------------------------- Static Methods
  // ----------------------------------------------------------- Inner Classes
}

/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.core.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author sgutti
 * @date Nov 16, 2019 11:02:28 PM
 */
public abstract class BaseDTO implements Serializable {

  // --------------------------------------------------------------- Constants
  private static final long serialVersionUID = -3294777492523737939L;
  // --------------------------------------------------------- Class Variables
  // ----------------------------------------------------- Static Initializers
  // ------------------------------------------------------ Instance Variables
  private LocalDate createdDt;
  private String createdBy;
  private LocalDate updatedDt;
  private String updatedBy;

  // ------------------------------------------------------------ Constructors
  /**
   * Create a new <code>BaseVO</code>
   */
  public BaseDTO() {
    super();
  }

  // ---------------------------------------------------------- Public Methods
  /**
   * @return Returns the createdDt.
   */
  public LocalDate getCreatedDt() {
    return createdDt;
  }

  /**
   * @param createdDt The createdDt to set.
   */
  public void setCreatedDt(LocalDate createdDt) {
    this.createdDt = createdDt;
  }

  /**
   * @return Returns the createdBy.
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * @param createdBy The createdBy to set.
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * @return Returns the updatedDt.
   */
  public LocalDate getUpdatedDt() {
    return updatedDt;
  }

  /**
   * @param updatedDt The updatedDt to set.
   */
  public void setUpdatedDt(LocalDate updatedDt) {
    this.updatedDt = updatedDt;
  }

  /**
   * @return Returns the updatedBy.
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * @param updatedBy The updatedBy to set.
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  // ------------------------------------------------------- Protected Methods
  // --------------------------------------------------------- Default Methods
  // --------------------------------------------------------- Private Methods
  // ---------------------------------------------------------- Static Methods
  // ----------------------------------------------------------- Inner Classes
}

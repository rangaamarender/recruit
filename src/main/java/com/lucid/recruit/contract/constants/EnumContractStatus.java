/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.constants;

import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.worker.entity.WorkerStatusCode;

public enum EnumContractStatus {

    //The MSA is in the initial stages of creation and not finalized
    DRAFT,
    //The MSA is currently in effect and operational(Green #008000)
    ACTIVE,
    //The MSA is under review by relevant parties. (Blue #0000FF)
    PENDING_REVIEW,
    //The MSA has been reviewed and approved. (Green #008000)
    APPROVED,
    //The MSA's end date is approaching.
    EXPIRING_SOON,
    //The MSA has reached its end date and is no longer active. (Red #FF0000)
    EXPIRED,
    //The MSA has been canceled before its end date. (Gray #808080)
    CANCELLED,
    //The MSA has been rejected and will not be executed. (Red #FF0000)
    REJECTED,
    //The MSA requires pending documents or information.
    DOCUMENTS_PENDING,

	//The MSA was terminated,
	TERMINATED;
    public static boolean isInitState(EnumContractStatus status) {
        if (status != EnumContractStatus.DRAFT && status != EnumContractStatus.PENDING_REVIEW) {
            return false;
        }
        return true;
    }

}

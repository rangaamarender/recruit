package com.lucid.recruit.contract.service;

import com.lucid.core.base.BaseService;
import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.dto.*;
import com.lucid.recruit.contract.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


public interface ContractService extends BaseService {
    ContractDTO createContractMSA(ContractDTO contract);

    ContractAccountDTO createContractAccount(String contractID, ContractAccountDTO contractAccountDTO);

    ContractDiscountDTO createContractDiscount(String contractAccId, ContractDiscountDTO contractDiscount);

    ContractExpenseDTO createContractExpense(String contractWOGroupID, ContractExpenseDTO contractExpense);

    ContractWorkOrderDTO createContractWorkOrder(String contractWOGroupID, ContractWorkOrderDTO contractWorkOrderDTO);

    ContractDTO updateContractMSA(String contractID, ContractDTO contract);

    ContractAccountDTO updateContractAccount(String contractAccountId, ContractAccountDTO contractAccountDTO);

    ContractWorkOrderDTO updateContractWorkOrder(String workOrderID, ContractWorkOrderDTO contractWorkOrderDTO);

    ContractDTO getContractMSA(String contractID);

    ContractAccountDTO getContractAccount(String contractWOGroupID);

    ContractWorkOrderDTO getContractWorkOrder(String workOrderID);

    Page<ContractSummaryDTO> getAllContracts(Integer offset, Integer limit, String contractName, String organizationID);

    Page<WorkOrderSummaryDTO> getAllWorkOrders(Integer offset, Integer limit, String wbsCode, String organizationID);

    boolean updateWOStatus(String workOrderID, EnumContractWOStatus status);

    ContractDocumentDTO createContractDocument(String contractID,ContractDocumentDTO contractDocumentDTO);

    WorkOrderDocumentDTO createWorkOrderDocument(String workOrderID, WorkOrderDocumentDTO workOrderDocumentDTO);

    String uploadContractDocFile(String contractDocID, MultipartFile file);

    String uploadWorkOrderDocFile(String woDocID, MultipartFile file);

    String deleteContractDocument(String contractDocID);

    String deleteWorkOrderDocument(String woDocID);

    byte[] retrieveContractRelatedFile(String url);

    ContractDocument retrieveContractDocument(String contractDocID);


    WorkOrderDocument retrieveWorkOrderDocument(String woDocID);

    String deleteDiscount(String contractAccountId, String discountId, LocalDate endDate);

    ContractDiscountDTO updateContractDiscount(String contractAccountId, String discountId, ContractDiscountDTO contractDiscountDTO);

    List<ContractAccountDTO> getContractAccounts(String contractId);
}
package com.lucid.recruit.contract.service;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.contract.constants.EnumBillPeriodUnit;
import com.lucid.recruit.contract.constants.EnumDiscountStepType;
import com.lucid.recruit.contract.constants.EnumContractStatus;
import com.lucid.recruit.contract.dto.*;
import com.lucid.recruit.contract.entity.*;
import com.lucid.recruit.contract.repo.*;
import com.lucid.recruit.docs.constants.EnumDocStatus;
import com.lucid.recruit.docs.dto.BaseDocAttributesDTO;
import com.lucid.recruit.docs.entity.DocAttributeDef;
import com.lucid.recruit.docs.entity.DocumentDef;
import com.lucid.recruit.docs.validations.DocumentDefValidatior;
import com.lucid.recruit.org.constants.OrgStatus;
import com.lucid.recruit.org.entity.Organization;
import com.lucid.recruit.org.entity.OrganizationStatus;
import com.lucid.recruit.org.repo.OrganizationRepo;
import com.lucid.recruit.org.repo.OrganizationStatusRepo;
import com.lucid.recruit.org.service.OrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ContractValidator {

    private static final Logger log = LoggerFactory.getLogger(ContractValidator.class);

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ContractAccountRepo contractAccountRepo;

    @Autowired
    private ContractWorkOrderRepo contractWorkOrderRepo;

    @Autowired
    private DocumentDefValidatior docDefValidator;

    @Autowired
    private ContractDocumentRepo contractDocumentRepo;

    @Autowired
    private WorkOrderDocRepo workOrderDocRepo;

    @Autowired
    private OrganizationRepo organizationRepo;

    @Autowired
    private OrganizationStatusRepo organizationStatusRepo;

    public void validateContract(ContractDTO contractDTO){
        //validate name
        validateName(contractDTO.getContractID(),contractDTO.getContractName());
        if(contractDTO.getRelatedOrg() != null || !StringUtils.isAllBlank(contractDTO.getRelatedOrg().getOrganizationID())){
            validateOrganization(contractDTO.getRelatedOrg().getOrganizationID(),contractDTO.getStartDate());
        }
        else{
            log.error("Organization required to createContract");
            throw new InvalidDataException(ErrorCodes.CNT_BV_0001,"relatedOrg","organizationID",contractDTO.getRelatedOrg().getOrganizationID(),"Organization required to createContract");
        }
        if(!StringUtils.isAllBlank(contractDTO.getBillingCurrCode())){
            validateBillingCurrency(contractDTO.getBillingCurrCode());
        }
        if(contractDTO.getContractAccounts() == null || contractDTO.getContractAccounts().isEmpty()){
            throw new InvalidDataException(ErrorCodes.CNT_BV_0042,"contract","contractAccounts",null,"ContractAccountDetails mandatory to create contractMSA");
        }
    }

    public void validateName(String contractId,String contractName){
        //check name already exists or not
        Contract contract = contractRepo.findByContractName(contractName);
        if(contract != null){
            if(contractId == null || !contract.getContractID().equals(contractId)){
                throw new InvalidDataException(ErrorCodes.CNT_BV_0037,"contract","contractName",contractName,"Contract already exists with name:"+contractName);
            }
        }
    }

    public void validateBillingCurrency(String currencyCode){
        if(Currency.getInstance(currencyCode) == null){
            log.error("InValid billingCurrencyCode:"+currencyCode);
            throw new InvalidDataException(ErrorCodes.CNT_BV_0006,"contract","billingCurrCode",currencyCode,"InValid billingCurrencyCode:"+currencyCode);
        }
    }

    public Organization getRelatedOrganization(String organizationId){
       return organizationRepo.findById(organizationId).orElseThrow(() -> {throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Organization by id:"+organizationId+" not found");});
    }

    public OrganizationStatus getLatestOrgStatus(String organizationID, LocalDate effectiveDate){
        return organizationStatusRepo.getLatestOrgStatus(organizationID,effectiveDate);
    }
    public void validateOrganization(String organizationId,LocalDate effectiveDate) {
        OrganizationStatus latestOrgStatus = getLatestOrgStatus(organizationId,effectiveDate);
        if(latestOrgStatus == null){
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Organization by id:"+organizationId+" not found");
        }
        Organization organization = latestOrgStatus.getOrganization();
        if (!latestOrgStatus.getStatusCode().equals(OrgStatus.ACTIVE)) {
            log.warn("Organization " + organization.getName() + " not allowed to create contract");
            throw new InvalidDataException(ErrorCodes.CNT_BV_0003, "relatedOrg", "organizationID", organizationId, "Organization " + organization.getName() + " not in active status");
        }
        if (StringUtils.isAllBlank(organization.getTaxId())) {
            log.error("EIN number not mapped to this organization:"+organization.getName());
            throw new InvalidDataException(ErrorCodes.CNT_BV_0002, "relatedOrg", "organizationID", organizationId, "EIN number not mapped to this organization:"+organization.getName());
        }
    }

    private void validateInitialStatus(EnumContractStatus contractStatus) {
      if(!EnumContractStatus.isInitState(contractStatus)){
          log.error("Contract not allowed to created with status:" + contractStatus.toString());
          throw new InvalidDataException(ErrorCodes.CNT_BV_0007, "contract", "contractStatus", contractStatus.toString(), "Contract not allowed to created with status:" + contractStatus.toString());
      }
    }

    public Contract findContract(String contractID){
        Optional<Contract> optionalContract = contractRepo.findById(contractID);
        if(optionalContract.isEmpty()){
            log.error("Contract by id:"+contractID+" not found");
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"Contract by id:"+contractID+" not found");
        }
        return optionalContract.get();
    }

    public void validateDiscountSteps(List<ContractDiscountStep> contractDiscountSteps) {
        int stepNumber =1;
        ContractDiscountStep preDiscountStep = null;
        for(Iterator<ContractDiscountStep> discountStepIterator = contractDiscountSteps.iterator();discountStepIterator.hasNext();){
            ContractDiscountStep contractDiscountStep =(ContractDiscountStep)discountStepIterator.next();
            if(preDiscountStep != null) {
               if(contractDiscountStep.getStepThreshold() <= preDiscountStep.getStepThreshold()){
                   log.warn("StepThreshold values must be unique");
                   throw new InvalidDataException(ErrorCodes.CNT_BV_0009, "discountStep", "stepThreshold", String.valueOf(contractDiscountStep.getStepThreshold()), "StepThreshold values must be unique");
               }
            }
            else if(contractDiscountStep.getStepThreshold() >0){
                log.warn("FirstStepThreshold must start with '0'");
                throw new InvalidDataException(ErrorCodes.CNT_BV_0008, "discountStep", "stepThreshold", String.valueOf(contractDiscountStep.getStepThreshold()), "FirstStepThreshold must start with '0'");
            }
            if (contractDiscountStep.getDiscountType() == EnumDiscountStepType.MONETARY) {
                if (contractDiscountStep.getDiscountAmount() == null || contractDiscountStep.getDiscountAmount() < 0) {
                    log.warn("Invalid step discount amount");
                    throw new InvalidDataException(ErrorCodes.CNT_BV_0010, "discountStep", "discountAmount", String.valueOf((Object) contractDiscountStep.getDiscountAmount()), "Invalid step discount amount");
                }
                contractDiscountStep.setDiscountPct(null);
            } else if (contractDiscountStep.getDiscountType() == EnumDiscountStepType.PERCENTAGE) {
                if (contractDiscountStep.getDiscountPct() == null || contractDiscountStep.getDiscountPct() < 0) {
                    log.warn("Invalid discount percentage");
                    throw new InvalidDataException(ErrorCodes.CNT_BV_0011, "discountStep", "discountPct", String.valueOf((Object) contractDiscountStep.getDiscountPct()), "Invalid discount percentage");
                }
                contractDiscountStep.setDiscountAmount(null);
            }
            contractDiscountStep.setStepNumber(stepNumber++);
            preDiscountStep = contractDiscountStep;
        }
    }

    public void validateContractClients(List<ContractClient> contractClients) {
        contractClients.sort(ContractClient.createContractClientLambdaComparator());
        ContractClient prvContractClient = null;
        int i = 1;
        for(Iterator<ContractClient> contractClientIterator=contractClients.iterator();contractClientIterator.hasNext();){
            ContractClient contractClient = contractClientIterator.next();
            if(contractClient.getClientSequence() != i) {
                log.warn("Invalid client sequence");
               throw new InvalidDataException(ErrorCodes.CNT_BV_0012,"contractClient","clientSequence",String.valueOf(contractClient.getClientSequence()),"Invalid client sequence");
            }
            if(prvContractClient != null){
                prvContractClient.setEndClientBoo(false);
            }
            else{
                contractClient.setEndClientBoo(true);
            }
            prvContractClient=contractClient;
            i++;
        }
    }

    public ContractAccount findContractAccount(String contractWOGroupID) {
        return contractAccountRepo.findById(contractWOGroupID).orElseThrow(() ->
        {
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404, "ContractAccount with id:" + contractWOGroupID + " not found");
        });
    }

    public void validateContractDiscounts(List<ContractDiscount> contractDiscounts) {
        int i = 1;
        for(ContractDiscount contractDiscount:contractDiscounts){
            if(contractDiscount.getPriority() != i){
                log.warn("Invalid priority order:"+contractDiscount.getPriority());
                throw new InvalidDataException(ErrorCodes.CNT_BV_0013,"contractDiscount","priority",String.valueOf(contractDiscount.getPriority()),"Invalid priority order:"+contractDiscount.getPriority());
            }
            i++;
        }
    }

    public ContractWorkOrder findContractWorkOrder(String workOrderID) {
        return contractWorkOrderRepo.findById(workOrderID).orElseThrow(() -> {
            throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404
                    , "ContractWorkOrder by id:" + workOrderID + " not found");
        });
    }

    public void validateChargeCodeRates(List<ChargeCodeRate> chargeCodeRates) {
        int stepNumber =1;
        ChargeCodeRate preChargeCodeRate = null;
        for(Iterator<ChargeCodeRate> chargeCodeRateIterator = chargeCodeRates.iterator();chargeCodeRateIterator.hasNext();){
            ChargeCodeRate chargeCodeRate =(ChargeCodeRate)chargeCodeRateIterator.next();
            if(preChargeCodeRate != null) {
                if(chargeCodeRate.getStepThreshold() <= preChargeCodeRate.getStepThreshold()){
                    log.warn("StepThreshold values must be unique");
                    throw new InvalidDataException(ErrorCodes.CNT_BV_0014, "chargeCodeRate", "stepThreshold", String.valueOf(chargeCodeRate.getStepThreshold()), "StepThreshold values must be unique");
                }
            }
            else if(chargeCodeRate.getStepThreshold() >0){
                log.warn("FirstStepThreshold must start with '0'");
                throw new InvalidDataException(ErrorCodes.CNT_BV_0015, "chargeCodeRate", "stepThreshold", String.valueOf(chargeCodeRate.getStepThreshold()), "FirstStepThreshold must start with '0'");
            }
            chargeCodeRate.setStepNumber(stepNumber++);
            preChargeCodeRate = chargeCodeRate;
        }
    }

    public void validateContractSuperVisors(List<ContractSupervisor> contractSupervisors) {
        ContractSupervisor prvSupervisor = null;
        for (Iterator<ContractSupervisor> iterator = contractSupervisors.iterator(); iterator.hasNext(); ) {
            ContractSupervisor contractSupervisor =(ContractSupervisor)iterator.next();
            if(prvSupervisor != null && prvSupervisor.getRole().getSupervisorRoleID().equals(contractSupervisor.getRole().getSupervisorRoleID())){
                if(prvSupervisor.getStartDate().equals(contractSupervisor.getStartDate())){
                    log.warn("More than one supervisor with same role and same start date not allowed");
                    throw new InvalidDataException(ErrorCodes.CNT_BV_0016,"contractSupervisor","startDate",contractSupervisor.getStartDate().toString(),"More than one supervisor with same role and same start date not allowed");
                }
                prvSupervisor.setEndDate(contractSupervisor.getStartDate().minusDays(1));
            }
        }
    }

    public ContractDocument validateContractDocument(ContractDocumentDTO contractDocumentDTO) {

        // Check if worker doc ID i.e. if not null means uploading the auto assigned doc
        // And validate if the status of auto assigned is pending
        ContractDocument contractDocument = null;
        if (contractDocumentDTO.getDocID() != null && !contractDocumentDTO.getDocID().isEmpty()) {
            Optional<ContractDocument> optionalContractDocument = contractDocumentRepo.findById(contractDocumentDTO.getDocID());
            if (optionalContractDocument.isEmpty()) {
                log.error("Contract Document by id " + contractDocumentDTO.getDocID() + " not found");
                throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
                        "Contract Document by id " + contractDocumentDTO.getDocID() + " not found");
            }
            if (optionalContractDocument.get().getStatus().compareTo(EnumDocStatus.Pending) != 0) {
                log.error("Contract Document by id " + contractDocumentDTO.getDocID() + " not in pending status");
                throw new InvalidDataException(ErrorCodes.CNT_BV_0004, "contractDocument", "docID",
                        contractDocumentDTO.getDocID(),
                        "Contract Document by id " + contractDocumentDTO.getDocID()
                                + " not in pending status, current status " + optionalContractDocument.get().getStatus());
            }
            contractDocument = optionalContractDocument.get();
        }
        // get the document definition
        DocumentDef documentDef = docDefValidator.getDocumentDef(contractDocumentDTO.getDocumentDef().getDocumentDefID());
        List<BaseDocAttributesDTO> baseAttrDTO = new ArrayList<>();
        contractDocumentDTO.getContractDocAttributes().forEach(contractDocAttr -> {
            baseAttrDTO.add(contractDocAttr);
        });
        // Now check the doc type and its related values
        docDefValidator.validateDocIssueAndExpiryDates(documentDef, contractDocumentDTO.getIssueDate(),
                contractDocumentDTO.getExpiryDate());
        // Now check all the mandatory attributes are passed
        List<DocAttributeDef> docAttrDef = docDefValidator.validateDocRequiredAttributes(documentDef, baseAttrDTO);
        // Now check the attributes are as per definition
        docDefValidator.validateDocAttributes(documentDef, docAttrDef, baseAttrDTO);
        return contractDocument;
    }

    public WorkOrderDocument validateWorkOrderDocument(WorkOrderDocumentDTO workOrderDocumentDTO) {
        // Check if worker doc ID i.e. if not null means uploading the auto assigned doc
        // And validate if the status of auto assigned is pending
        WorkOrderDocument workOrderDocument = null;
        if (workOrderDocumentDTO.getDocID() != null && !workOrderDocumentDTO.getDocID().isEmpty()) {
            Optional<WorkOrderDocument> optionalWorkOrderDocument = workOrderDocRepo.findById(workOrderDocumentDTO.getDocID());
            if (optionalWorkOrderDocument.isEmpty()) {
                log.error("WorkOrder Document by id " + workOrderDocumentDTO.getDocID() + " not found");
                throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,
                        "WorkOrder Document by id " + workOrderDocumentDTO.getDocID() + " not found");
            }
            if (optionalWorkOrderDocument.get().getStatus().compareTo(EnumDocStatus.Pending) != 0) {
                log.error("WorkOrder Document by id " + workOrderDocumentDTO.getDocID() + " not in pending status");
                throw new InvalidDataException(ErrorCodes.CNT_BV_0004, "contractDocument", "docID",
                        workOrderDocumentDTO.getDocID(),
                        "WorkOrder Document by id " + workOrderDocumentDTO.getDocID()
                                + " not in pending status, current status " + optionalWorkOrderDocument.get().getStatus());
            }
            workOrderDocument = optionalWorkOrderDocument.get();
        }
        // get the document definition
        DocumentDef documentDef = docDefValidator.getDocumentDef(workOrderDocumentDTO.getDocumentDef().getDocumentDefID());
        List<BaseDocAttributesDTO> baseAttrDTO = new ArrayList<>();
        workOrderDocumentDTO.getWorkOrderDocAttributes().forEach(workerDocAtrr -> {
            baseAttrDTO.add(workerDocAtrr);
        });
        // Now check the doc type and its related values
        docDefValidator.validateDocIssueAndExpiryDates(documentDef, workOrderDocumentDTO.getIssueDate(),
                workOrderDocumentDTO.getExpiryDate());
        // Now check all the mandatory attributes are passed
        List<DocAttributeDef> docAttrDef = docDefValidator.validateDocRequiredAttributes(documentDef, baseAttrDTO);
        // Now check the attributes are as per definition
        docDefValidator.validateDocAttributes(documentDef, docAttrDef, baseAttrDTO);
        return workOrderDocument;
    }

    public WorkOrderDocument getWorkOrderDocument(String woDocID) {
        return workOrderDocRepo.findById(woDocID).orElseThrow(() -> {throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"WorkOrderDoc by id:"+woDocID+" not found");});
    }

    public ContractDocument getContractDoc(String contractDocID){
        return contractDocumentRepo.findById(contractDocID).orElseThrow(() -> {throw new EntityNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND_404,"ContractDoc by id:"+contractDocID+" not found");});
    }


    public void validateContractAccountNames(List<ContractAccount> contractAccounts) {
        Set<String> accountNames = new HashSet<>();
        Set<String> duplicateNames = new HashSet<>();
        for(ContractAccount contractAccount:contractAccounts) {
            if (!accountNames.add(contractAccount.getContractAccountName())) {
                duplicateNames.add(contractAccount.getContractAccountName());
            }
        }
        if(accountNames.size() < contractAccounts.size()){
            throw new InvalidDataException(ErrorCodes.CNT_BV_0041,"contractAccount","contractAccountName",null,"ContractAccount already exists with name:"+duplicateNames.toString());
        }
    }

    public void validateContractAccountStartDate(ContractAccount contractAccount, Contract contract) {
        LocalDate startDate = getStartDate(contractAccount.getStartDate(),contract.getStartDate());
        if(startDate.isBefore(contract.getStartDate())){
            throw new InvalidDataException(ErrorCodes.CNT_BV_0028,"contract","contractAccount",contractAccount.getStartDate().toString(),"Account startDate not be before contract startDate");
        }
        contractAccount.setStartDate(startDate);
    }

    public void validateContractBillingDates(ContractBillingDetails contractBillingDetails, ContractAccount contractAccount) {
        LocalDate startDate = getStartDate(contractBillingDetails.getStartDate(),contractAccount.getStartDate());
        if(startDate.isBefore(contractAccount.getStartDate())){
            throw new InvalidDataException(ErrorCodes.CNT_BV_0038,"contractBillingDetails","startDate",startDate.toString(),"ContractBillingDetails startDate:"+startDate+" not before contract startDate:"+contractAccount.getStartDate());
        }
        contractBillingDetails.setStartDate(startDate);
    }

    public void validateContractDiscountDates(ContractDiscount contractDiscount, ContractAccount contractAccount) {
        LocalDate startDate = getStartDate(contractDiscount.getStartDate(),contractAccount.getStartDate());
        if(startDate.isBefore(contractAccount.getStartDate())){
            throw new InvalidDataException(ErrorCodes.CNT_BV_0039,"contractDiscounts","startDate",startDate.toString(),"ContractDiscount startDate:"+startDate+" not before contract startDate:"+contractAccount.getStartDate());
        }
    }

    public void validateContractExpenseDates(ContractExpense contractExpense, ContractAccount contractAccount) {
        LocalDate startDate=getStartDate(contractExpense.getStartDate(),contractAccount.getStartDate());
        if(startDate.isBefore(contractAccount.getStartDate())){
            throw new InvalidDataException(ErrorCodes.CNT_BV_0040,"contractExpense","startDate",startDate.toString(),"ContractExpense startDate:"+startDate+" not before contract startDate:"+contractAccount.getStartDate());
        }
    }
    private LocalDate getStartDate(LocalDate childStartDate,LocalDate parentStartDate){
        LocalDate startDate = null;
        if(childStartDate != null){
            startDate = childStartDate;
        }
        else{
            if(parentStartDate.equals(LocalDate.now()) || parentStartDate.isAfter(LocalDate.now())) {
                startDate=parentStartDate;
            }
            else {
                startDate = LocalDate.now();
            }
        }
        return startDate;
    }

    public void validateBillingDetails(ContractBillingDetailsDTO contractBillingDetailsDTO) {
        if(contractBillingDetailsDTO.getBillPeriodUnits().equals(EnumBillPeriodUnit.WEEKLY)){
            if(contractBillingDetailsDTO.getBillCycle()>7 || contractBillingDetailsDTO.getBillCycle()<1){
                throw new InvalidDataException(ErrorCodes.CNT_BV_0043,"contractAccount","billingDetials",String.valueOf(contractBillingDetailsDTO.getBillCycle()),"BillCycle must between 1-7 for billPeriodUnit"+EnumBillPeriodUnit.WEEKLY);
            }
        }
        else{
            if(contractBillingDetailsDTO.getBillCycle()>28 || contractBillingDetailsDTO.getBillCycle()<1){
                throw new InvalidDataException(ErrorCodes.CNT_BV_0043,"contractAccount","billingDetials",String.valueOf(contractBillingDetailsDTO.getBillCycle()),"BillCycle must between 1-28 for billPeriodUnit"+EnumBillPeriodUnit.MONTHLY);
            }
        }
    }
}

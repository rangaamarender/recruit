package com.lucid.recruit.contract.service;

import com.lucid.recruit.contract.dto.*;
import com.lucid.recruit.contract.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractModelConverter {


    @Autowired
    private ModelMapper modelMapper;

    // convert dto to entity and vice versa code
    public Contract convertToEntity(ContractDTO requestData) {
        return modelMapper.map(requestData, Contract.class);
    }

    public ContractDTO convertToDTO(Contract contract) {
        return modelMapper.map(contract, ContractDTO.class);
    }

    public ContractAccount convertToEntity(ContractAccountDTO contractAccountDTO) {
        return modelMapper.map(contractAccountDTO, ContractAccount.class);
    }

    public ContractAccountDTO convertToDTO(ContractAccount contractAccount) {
        return modelMapper.map(contractAccount, ContractAccountDTO.class);
    }

    public ContractDiscount convertToEntity(ContractDiscountDTO contractDiscountDTO) {
        return modelMapper.map(contractDiscountDTO, ContractDiscount.class);
    }

    public ContractDiscountDTO convertToDTO(ContractDiscount contractDiscount) {
        return modelMapper.map(contractDiscount, ContractDiscountDTO.class);
    }

    public ContractBillingDetails convertToEntity(ContractBillingDetailsDTO contractBillingDetails) {
        return modelMapper.map(contractBillingDetails, ContractBillingDetails.class);
    }

    public ContractBillingDetailsDTO convertToDTO(ContractBillingDetails contractBillingDetails) {
        return modelMapper.map(contractBillingDetails, ContractBillingDetailsDTO.class);
    }

    public ContractWorkOrder convertToEntity(ContractWorkOrderDTO contractWorkOrderDTO) {
        return modelMapper.map(contractWorkOrderDTO, ContractWorkOrder.class);
    }

    public ContractWorkOrderDTO convertToDTO(ContractWorkOrder contractWorkOrder) {
        return modelMapper.map(contractWorkOrder, ContractWorkOrderDTO.class);
    }

    public ContractSupervisor convertToEntity(ContractSupervisorDTO contractSupervisorDTO) {
        return modelMapper.map(contractSupervisorDTO, ContractSupervisor.class);
    }

    public ContractSupervisorDTO convertToDTO(ContractSupervisor contractSupervisor) {
        return modelMapper.map(contractSupervisor, ContractSupervisorDTO.class);
    }

    public ContractClient convertToEntity(ContractClientDTO contractClientDTO) {
        return modelMapper.map(contractClientDTO, ContractClient.class);
    }

    public ContractClientDTO convertToDTO(ContractClient contractClient) {
        return modelMapper.map(contractClient, ContractClientDTO.class);
    }

    public ContractWorkLocation convertToEntity(ContractWorkLocationDTO contractWorkLocationDTO) {
        return modelMapper.map(contractWorkLocationDTO, ContractWorkLocation.class);
    }

    public ContractWorkLocationDTO convertToDTO(ContractWorkLocation contractWorkLocation) {
        return modelMapper.map(contractWorkLocation, ContractWorkLocationDTO.class);
    }

    public WorkOrderChargeCode convertToEntity(WorkOrderChargeCodeDTO workOrderChargeCodeDTO) {
        return modelMapper.map(workOrderChargeCodeDTO, WorkOrderChargeCode.class);
    }

    public WorkOrderChargeCodeDTO convertToDTO(WorkOrderChargeCode workOrderChargeCode) {
        return modelMapper.map(workOrderChargeCode, WorkOrderChargeCodeDTO.class);
    }

    public ChargeCodeResource convertToEntity(ChargeCodeResourceDTO chargeCodeResourceDTO) {
        return modelMapper.map(chargeCodeResourceDTO, ChargeCodeResource.class);
    }

    public ChargeCodeResourceDTO convertToDTO(ChargeCodeResource chargeCodeResource) {
        return modelMapper.map(chargeCodeResource, ChargeCodeResourceDTO.class);
    }

    public WorkOrderExpenseBudget convertToEntity(ContractPayProfileDTO contractPayProfileDTO) {
        return modelMapper.map(contractPayProfileDTO, WorkOrderExpenseBudget.class);
    }

    public ContractPayProfileDTO convertToDTO(WorkOrderExpenseBudget contractPayProfile) {
        return modelMapper.map(contractPayProfile, ContractPayProfileDTO.class);
    }

    public ChargeCodeRate convertToEntity(ChargeCodeRateDTO chargeCodeRateDTO) {
        return modelMapper.map(chargeCodeRateDTO, ChargeCodeRate.class);
    }

    public ChargeCodeRateDTO convertToDTO(ChargeCodeRate chargeCodeRate) {
        return modelMapper.map(chargeCodeRate, ChargeCodeRateDTO.class);
    }

    public ContractDiscountStep convertToEntity(ContractDiscountStepDTO contractDiscountStepDTO){
        return modelMapper.map(contractDiscountStepDTO,ContractDiscountStep.class);
    }

    public ContractDiscountStepDTO convertToDTO(ContractDiscountStep contractDiscountStep){
        return modelMapper.map(contractDiscountStep,ContractDiscountStepDTO.class);
    }

    public ContractExpense convertToEntity(ContractExpenseDTO contractExpenseDTO){
        return modelMapper.map(contractExpenseDTO,ContractExpense.class);
    }

    public ContractExpenseDTO convertToDTO(ContractExpense contractExpense){
        return modelMapper.map(contractExpense,ContractExpenseDTO.class);
    }

    public ChargeCodeTasks convertToEntity(ChargeCodeTasksDTO chargeCodeTasksDTO){
        return modelMapper.map(chargeCodeTasksDTO,ChargeCodeTasks.class);
    }

    public ChargeCodeTasksDTO convertToDTO(ChargeCodeTasks chargeCodeTask){
        return modelMapper.map(chargeCodeTask,ChargeCodeTasksDTO.class);
    }

    public WorkOrderExpenseCodes convertToEntity(WorkOrderExpenseCodesDTO workOrderExpenseCodesDTO){
        return modelMapper.map(workOrderExpenseCodesDTO,WorkOrderExpenseCodes.class);
    }

    public WorkOrderExpenseCodesDTO convertToDTO(WorkOrderExpenseCodes workOrderExpenseCodes){
        return modelMapper.map(workOrderExpenseCodes,WorkOrderExpenseCodesDTO.class);
    }

    public WorkOrderExpenseBudget convertToEntity(WorkOrderExpenseBudgetDTO workOrderExpenseBudgetDTO){
        return modelMapper.map(workOrderExpenseBudgetDTO,WorkOrderExpenseBudget.class);
    }

    public WorkOrderExpenseBudgetDTO convertToDTO(WorkOrderExpenseBudgetDTO workOrderExpenseBudget){
        return modelMapper.map(workOrderExpenseBudget,WorkOrderExpenseBudgetDTO.class);
    }

    public WorkOrderSuppCharges convertToEntity(WorkOrderSuppChargesDTO workOrderSuppChargesDTO){
        return modelMapper.map(workOrderSuppChargesDTO,WorkOrderSuppCharges.class);
    }
    public WorkOrderSuppChargesDTO convertToDTO(WorkOrderSuppCharges workOrderSuppCharges){
        return modelMapper.map(workOrderSuppCharges,WorkOrderSuppChargesDTO.class);
    }

    public ContractResourceWorkerType convertToEntity(ContractResourceWorkerTypeDTO contractResourceWorkerTypeDTO){
        return modelMapper.map(contractResourceWorkerTypeDTO,ContractResourceWorkerType.class);
    }
    public ContractResourceWorkerTypeDTO convertToDTO(ContractResourceWorkerType contractResourceWorkerType){
        return modelMapper.map(contractResourceWorkerType,ContractResourceWorkerTypeDTO.class);
    }

    public ContractDocument convertToEntity(ContractDocumentDTO contractDocumentDTO){
        return modelMapper.map(contractDocumentDTO,ContractDocument.class);
    }
    public ContractDocumentDTO convertToDTO(ContractDocument contractDocument){
        return modelMapper.map(contractDocument,ContractDocumentDTO.class);
    }

    public ContractDocAttributes convertToEntity(ContractDocAttributesDTO contractDocAttributesDTO){
        return modelMapper.map(contractDocAttributesDTO,ContractDocAttributes.class);
    }
    public ContractDocAttributesDTO convertToDTO(ContractDocAttributes contractDocAttributes){
        return modelMapper.map(contractDocAttributes,ContractDocAttributesDTO.class);
    }

    public WorkOrderDocument convertToEntity(WorkOrderDocumentDTO workOrderDocumentDTO){
        return modelMapper.map(workOrderDocumentDTO,WorkOrderDocument.class);
    }
    public WorkOrderDocumentDTO convertToDTO(WorkOrderDocument workOrderDocument){
        return modelMapper.map(workOrderDocument,WorkOrderDocumentDTO.class);
    }

    public WorkOrderDocAttributes convertToEntity(WorkOrderDocAttributesDTO workOrderDocAttributesDTO){
        return modelMapper.map(workOrderDocAttributesDTO,WorkOrderDocAttributes.class);
    }
    public WorkOrderDocAttributesDTO convertToDTO(WorkOrderDocAttributes workOrderDocAttributes){
        return modelMapper.map(workOrderDocAttributes,WorkOrderDocAttributesDTO.class);
    }

    public WorkOrderResource convertToEntity(WorkOrderResourceDTO workOrderResourceDTO){
        return modelMapper.map(workOrderResourceDTO,WorkOrderResource.class);
    }
    public WorkOrderResourceDTO convertToDTO(WorkOrderResource workOrderResource){
        return modelMapper.map(workOrderResource,WorkOrderResourceDTO.class);
    }

}

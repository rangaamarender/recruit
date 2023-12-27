package com.lucid.recruit.contract.controller;

import com.lucid.core.exception.EntityNotFoundException;
import com.lucid.recruit.contract.constants.EnumContractWOStatus;
import com.lucid.recruit.contract.dto.*;
import com.lucid.recruit.contract.entity.ContractDocument;
import com.lucid.recruit.contract.entity.WorkOrderDocument;
import com.lucid.recruit.contract.service.ContractService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/raves/")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping("v1/contract")
    public ResponseEntity<Object> createContractMSA(@RequestBody @Valid ContractDTO contract) {
        return ResponseEntity.ok(contractService.createContractMSA(contract));
    }

    @PostMapping("v1/contract/{contractID}/account")
    public ResponseEntity<Object> createContractAccount(@PathVariable(name = "contractID") String contractID, @RequestBody @Valid ContractAccountDTO contractAccountDTO){
        return ResponseEntity.ok(contractService.createContractAccount(contractID, contractAccountDTO));
    }

    @GetMapping("v1/contract/{contractID}/account")
    public ResponseEntity<?> getContractAccounts(@PathVariable(name = "contractID") String contractID){
        return ResponseEntity.ok(contractService.getContractAccounts(contractID));
    }

    @PostMapping("v1/contract/account/{contractAccountId}/contractworkorder")
    public ResponseEntity<Object> createContractWorkOrder(@PathVariable(name = "contractAccountId") String contractAccountId, @RequestBody @Valid ContractWorkOrderDTO contractWorkOrderDTO){
        return ResponseEntity.ok(contractService.createContractWorkOrder(contractAccountId,contractWorkOrderDTO));
    }

    @PatchMapping("v1/contract/{contractID}")
    public ResponseEntity<Object> updateContractMSA(@PathVariable(name = "contractID")String contractID,@RequestBody ContractDTO contractDTO){
        return ResponseEntity.ok(contractService.updateContractMSA(contractID,contractDTO));
    }

    @PatchMapping("v1/contract/account/{contractAccountId}")
    public ResponseEntity<Object> updateContractAccount(@PathVariable(name = "contractAccountId") String contractAccountId,@RequestBody ContractAccountDTO contractAccountDTO){
        return ResponseEntity.ok(contractService.updateContractAccount(contractAccountId, contractAccountDTO));
    }

    @DeleteMapping("v1/contract/account/{contractAccountId}/discount/{discountId}")
    public ResponseEntity<?>  deleteDiscount(@PathVariable(name = "contractAccountId") String contractAccountId,@PathVariable(name = "discountId") String discountId,@RequestParam(name = "endDate") LocalDate endDate){
        return ResponseEntity.ok(contractService.deleteDiscount(contractAccountId,discountId,endDate));
    }
    @PostMapping("v1/contract/account/{contractAccountId}/discount")
    public ResponseEntity<?>  createDiscount(@PathVariable(name = "contractAccountId") String contractAccountId,@RequestBody @Valid ContractDiscountDTO contractDiscountDTO){
        return ResponseEntity.ok(contractService.createContractDiscount(contractAccountId,contractDiscountDTO));
    }

    @PatchMapping("v1/contract/account/{contractAccountId}/discount/{discountId}")
    public ResponseEntity<?>  updateDiscount(@PathVariable(name = "contractAccountId") String contractAccountId,
                                             @PathVariable(name = "discountId") String discountId,
                                             @RequestBody @Valid ContractDiscountDTO contractDiscountDTO){
        return ResponseEntity.ok(contractService.updateContractDiscount(contractAccountId,discountId,contractDiscountDTO));
    }

    @PatchMapping("v1/contract/account/workorder/{workOrderID}")
    public ResponseEntity<Object> updateWorkOrder(@PathVariable(name = "workOrderID")String workOrderId,@RequestBody ContractWorkOrderDTO contractWorkOrderDTO){
        return ResponseEntity.ok(contractService.updateContractWorkOrder(workOrderId,contractWorkOrderDTO));
    }

    @GetMapping("v1/contract/{contractID}")
    public ResponseEntity<Object> getContract(@PathVariable(name = "contractID")String contractID){
        return ResponseEntity.ok(contractService.getContractMSA(contractID));
    }

    @GetMapping("v1/contract/contract/{contractAccountId}")
    public ResponseEntity<Object> getContractAccount(@PathVariable(name = "contractAccountId")String contractAccountId){
        return ResponseEntity.ok(contractService.getContractAccount(contractAccountId));
    }

    @GetMapping("v1/contract/account/workorder/{workOrderID}")
    public ResponseEntity<Object> getContractWorkOrder(@PathVariable(name = "workOrderID") String workOrderID){
        return ResponseEntity.ok(contractService.getContractWorkOrder(workOrderID));
    }

    @PatchMapping("v1/contract/workorder/{workOrderID}/{status}")
    public ResponseEntity<Object> updateWorkOrderStatus(@PathVariable(name ="workOrderID") String workOrderID, @PathVariable(name = "status")EnumContractWOStatus status){
        return ResponseEntity.ok(contractService.updateWOStatus(workOrderID,status));
    }

    @GetMapping("v1/contract/summary")
    public ResponseEntity<Object> getContractSummary(@RequestParam (required = false) Integer offset
            ,@RequestParam (required = false) Integer limit
            ,@RequestParam (required = false) String name
            ,@RequestParam (required = false) String organizationID){

        return ResponseEntity.ok(contractService.getAllContracts(offset,limit,name,organizationID));
    }

    @GetMapping("v1/contract/workorder/summary")
    public ResponseEntity<Object> getContractWOSummary(@RequestParam (required = false) Integer offset
            ,@RequestParam (required = false) Integer limit
            ,@RequestParam (required = false) String wbsCode
            ,@RequestParam (required = false) String organizationID){

        return ResponseEntity.ok(contractService.getAllWorkOrders(offset,limit,wbsCode,organizationID));
    }

    @GetMapping(path = "v1/contract/document/{contractDocID}", produces = { "multipart/form-data" })
    public ResponseEntity<byte[]> getContractDocumentFile(@PathVariable(name = "contractDocID") @NotNull String contractDocID) {
        ContractDocument contractDocument = contractService.retrieveContractDocument(contractDocID);
        byte[] fileData = contractService.retrieveContractRelatedFile(contractDocument.getUrl());
        if (fileData != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + contractDocument.getFileName() + "\"").body(fileData);

        } else {
            throw new EntityNotFoundException("Contract document file by Id " + contractDocID + " file not found");
        }
    }

    @GetMapping(path = "v1/contract/workorder/document/{woDocID}", produces = { "multipart/form-data" })
    public ResponseEntity<byte[]> getWoDocumentFile(@PathVariable(name = "woDocID") @NotNull String woDocID) {
        WorkOrderDocument workOrderDocument = contractService.retrieveWorkOrderDocument(woDocID);
        byte[] fileData = contractService.retrieveContractRelatedFile(workOrderDocument.getUrl());
        if (fileData != null) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + workOrderDocument.getFileName() + "\"").body(fileData);

        } else {
            throw new EntityNotFoundException("WorkOrder document file by Id " + woDocID + " file not found");
        }
    }

    @PostMapping(path = "v1/contract/{contractID}/document", consumes = {
            "application/json" }, produces = "application/json")
    public ResponseEntity<Object> createContractDocument(@PathVariable(name = "contractID") @NotNull String contractID,
                                                      @RequestBody @Valid ContractDocumentDTO contractDocumentDTO) {
        return ResponseEntity.ok(contractService.createContractDocument(contractID, contractDocumentDTO));
    }

    @PostMapping(path = "v1/contract/workorder/{workOrderID}/document", consumes = {
            "application/json" }, produces = "application/json")
    public ResponseEntity<Object> createWoDocument(@PathVariable(name = "workOrderID") @NotNull String workOrderID,
                                                      @RequestBody @Valid WorkOrderDocumentDTO workOrderDocumentDTO) {
        return ResponseEntity.ok(contractService.createWorkOrderDocument(workOrderID, workOrderDocumentDTO));
    }

    @PostMapping(path = "v1/contract/{contractDocID}/documentfile", consumes = {
            "multipart/form-data" }, produces = "application/json")
    public ResponseEntity<Object> uploadContractDocumentFile(@PathVariable(name = "contractDocID") @NotNull String contractDocID,
                                                          @RequestParam("contractfile") MultipartFile file) {
        return ResponseEntity.ok(contractService.uploadContractDocFile(contractDocID,file));
    }

    @PostMapping(path = "v1/contract/workorder/{woDocID}/documentfile", consumes = {
            "multipart/form-data" }, produces = "application/json")
    public ResponseEntity<Object> uploadWorkOrderDocumentFile(@PathVariable(name = "woDocID") @NotNull String woDocID,
                                                             @RequestParam("workOrderfile") MultipartFile file) {
        return ResponseEntity.ok(contractService.uploadWorkOrderDocFile(woDocID,file));
    }


}

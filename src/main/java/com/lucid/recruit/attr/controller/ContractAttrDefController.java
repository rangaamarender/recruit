package com.lucid.recruit.attr.controller;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.ContractAttributeDefDTO;
import com.lucid.recruit.attr.service.ContractAttrDefService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/contractattr")
public class ContractAttrDefController {

    @Autowired
    public ContractAttrDefService contractAttrDefService;

    @PostMapping("v1/contractattrdef")
    public ResponseEntity<?> createContractAttrDef(@RequestBody @Valid ContractAttributeDefDTO contractAttributeDef){
        return ResponseEntity.ok(contractAttrDefService.createContractAttributeDef(contractAttributeDef));
    }

    @PatchMapping("v1/contractattrdef/{attrDefId}")
    public ResponseEntity<?> updateContractAttrDef(@PathVariable(name = "attrDefId",required = true) String attrDefId, @RequestBody @Valid ContractAttributeDefDTO contractAttributeDef){
        return ResponseEntity.ok(contractAttrDefService.updateContractAttributeDef(attrDefId,contractAttributeDef));
    }

    @PatchMapping("v1/contractattrdef/{attrDefId}/{status}")
    public ResponseEntity<?> updateContractAttrDefStatus(@PathVariable(name = "attrDefId",required = true) String attrDefId, @PathVariable(name = "status",required = true) @NotNull EnumAttributeStatus attributeStatus){
        return ResponseEntity.ok(contractAttrDefService.updateStatus(attrDefId,attributeStatus));
    }

    @GetMapping("v1/contractattrdef/{attrDefId}")
    public ResponseEntity<?> getContractAttrDef(@PathVariable(name ="attrDefId",required = true) @NotNull String attrDefId){
        return ResponseEntity.ok(contractAttrDefService.getContractAttributeDef(attrDefId));
    }

    @GetMapping("v1/contractattrdef")
    public ResponseEntity<?> getAllContractAttrDef(){
        return ResponseEntity.ok(contractAttrDefService.getAllContractAttributeDef());
    }

}

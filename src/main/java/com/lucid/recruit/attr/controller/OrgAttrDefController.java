package com.lucid.recruit.attr.controller;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.OrgAttributeDefDTO;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;
import com.lucid.recruit.attr.service.OrgAttrDefService;
import com.lucid.recruit.attr.service.WorkerAttrDefService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("*")
@RestController
@RequestMapping("api/orgattr")
public class OrgAttrDefController {

    @Autowired
    public OrgAttrDefService orgAttrDefService;

    @PostMapping("v1/orgattrdef")
    public ResponseEntity<?> createOrgAttrDef(@RequestBody @Valid OrgAttributeDefDTO orgAttributeDef){
        return ResponseEntity.ok(orgAttrDefService.createOrgAttributeDef(orgAttributeDef));
    }

    @PatchMapping("v1/orgattrdef/{attrDefId}")
    public ResponseEntity<?> updateOrgAttrDef(@PathVariable(name = "attrDefId",required = true) String attrDefId, @RequestBody @Valid OrgAttributeDefDTO orgAttributeDef){
        return ResponseEntity.ok(orgAttrDefService.updateOrgAttributeDef(attrDefId,orgAttributeDef));
    }

    @PatchMapping("v1/orgattrdef/{attrDefId}/{status}")
    public ResponseEntity<?> updateOrgAttrDefStatus(@PathVariable(name = "attrDefId",required = true) String attrDefId, @PathVariable(name = "status",required = true) @NotNull EnumAttributeStatus attributeStatus){
        return ResponseEntity.ok(orgAttrDefService.updateStatus(attrDefId,attributeStatus));
    }

    @GetMapping("v1/orgattrdef/{attrDefId}")
    public ResponseEntity<?> getOrgAttrDef(@PathVariable(name ="attrDefId",required = true) @NotNull String attrDefId){
        return ResponseEntity.ok(orgAttrDefService.getOrgAttributeDef(attrDefId));
    }

    @GetMapping("v1/orgattrdef")
    public ResponseEntity<?> getAllOrgAttrDef(){
        return ResponseEntity.ok(orgAttrDefService.getAllOrgAttributeDef());
    }

}

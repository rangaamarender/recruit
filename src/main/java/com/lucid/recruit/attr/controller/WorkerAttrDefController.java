package com.lucid.recruit.attr.controller;

import com.lucid.recruit.attr.constants.EnumAttributeStatus;
import com.lucid.recruit.attr.dto.WorkerAttributeDefDTO;
import com.lucid.recruit.attr.service.WorkerAttrDefService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/workerattr")
public class WorkerAttrDefController {

    @Autowired
    public WorkerAttrDefService workerAttrDefService;

    @PostMapping("v1/workerattrdef")
    public ResponseEntity<?> createWorkerAttrDef(@RequestBody @Valid WorkerAttributeDefDTO workerAttributeDef){
        return ResponseEntity.ok(workerAttrDefService.createWorkerAttributeDef(workerAttributeDef));
    }

    @PatchMapping("v1/workerattrdef/{attrDefId}")
    public ResponseEntity<?> updateWorkerAttrDef(@PathVariable(name = "attrDefId",required = true) String attrDefId, @RequestBody @Valid WorkerAttributeDefDTO workerAttributeDef){
        return ResponseEntity.ok(workerAttrDefService.updateWorkerAttributeDef(attrDefId,workerAttributeDef));
    }

    @PatchMapping("v1/workerattrdef/{attrDefId}/{status}")
    public ResponseEntity<?> updateWorkerAttrDefStatus(@PathVariable(name = "attrDefId",required = true) String attrDefId, @PathVariable(name = "status",required = true) @NotNull EnumAttributeStatus attributeStatus){
        return ResponseEntity.ok(workerAttrDefService.updateStatus(attrDefId,attributeStatus));
    }

    @GetMapping("v1/workerattrdef/{attrDefId}")
    public ResponseEntity<?> getWorkerAttrDef(@PathVariable(name ="attrDefId",required = true) @NotNull String attrDefId){
        return ResponseEntity.ok(workerAttrDefService.getWorkerAttributeDef(attrDefId));
    }

    @GetMapping("v1/workerattrdef")
    public ResponseEntity<?> getAllWorkerAttrDef(@RequestParam(name = "status",required = false)EnumAttributeStatus status){
        return ResponseEntity.ok(workerAttrDefService.getAllWorkerAttributeDef(status));
    }

}

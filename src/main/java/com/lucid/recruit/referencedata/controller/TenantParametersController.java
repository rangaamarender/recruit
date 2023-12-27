package com.lucid.recruit.referencedata.controller;

import com.lucid.recruit.referencedata.dto.TenantParametersDTO;
import com.lucid.recruit.referencedata.service.TenantParametersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping("/api/raves")
@Tag(name = "TenantParameters", description = "Tenant API")
@RestController
public class TenantParametersController {
    @Autowired
    private TenantParametersService tntpmterService;

    @PostMapping("v1/tenantparams")
    public ResponseEntity<Object> createTntParameter(@RequestBody @Valid TenantParametersDTO tntParametersDTO) {
        return ResponseEntity.ok(tntpmterService.createTenantParameter(tntParametersDTO));
    }

    @GetMapping("v1/tenantparams/{name}")
    public TenantParametersDTO getTntPmeterByName(
            @RequestParam("name") String name) {
        return tntpmterService.findTenantParameterByName(name);
    }

    @GetMapping("v1/tenantparams")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(tntpmterService.getAllTenantParameters());
    }

}

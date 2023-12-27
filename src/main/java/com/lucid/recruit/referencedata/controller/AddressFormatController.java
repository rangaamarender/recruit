package com.lucid.recruit.referencedata.controller;

import com.lucid.core.exception.ErrorCodes;
import com.lucid.core.exception.InvalidDataException;
import com.lucid.recruit.referencedata.constants.EnumAddressLines;
import com.lucid.recruit.referencedata.dto.AddressFormatDTO;
import com.lucid.recruit.referencedata.service.AddressFormatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/raves/")
public class AddressFormatController {

    @Autowired
    private AddressFormatService addressFormatService;

    @PostMapping("v1/addressformat")
    public ResponseEntity<Object> createAddressFormat(@RequestBody  @Valid AddressFormatDTO addressFormat){
        return ResponseEntity.ok(addressFormatService.createAddressFormat(addressFormat));
    }

    @PatchMapping("v1/addressformat/{countryCode}")
    public ResponseEntity<Object> updateAddressFormat(@PathVariable(name = "countryCode") @NotNull String countryCode, @RequestBody  @Valid AddressFormatDTO addressFormat){
        return ResponseEntity.ok(addressFormatService.updateAddressFormat(countryCode,addressFormat));
    }

    @GetMapping("v1/addressformat/{countryCode}")
    public ResponseEntity<Object> getAddressFormat(@PathVariable(name ="countryCode") String countryCode){
        return ResponseEntity.ok(addressFormatService.getAddressFormat(countryCode));
    }

    @GetMapping("v1/addressformat/addresslines")
    public ResponseEntity<Object> getAddressLines(){
        return ResponseEntity.ok(EnumAddressLines.getAllAddressLineDTO());
    }

    @GetMapping("v1/addressformat/country")
    public ResponseEntity<Object> getCountrys(){
        return ResponseEntity.ok(addressFormatService.getAddrFormatCountrys());
    }

}

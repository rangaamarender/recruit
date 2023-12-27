package com.lucid.recruit.referencedata.service;

import com.lucid.recruit.referencedata.dto.TenantParametersDTO;
import java.time.LocalDate;
import java.util.List;

public interface TenantParametersService {

    TenantParametersDTO createTenantParameter(TenantParametersDTO tenantParametersDTO);
    TenantParametersDTO findTenantParameterByName(String name);

    List<TenantParametersDTO> getAllTenantParameters();
}

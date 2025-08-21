package com.inditex.code.prices.domain.mapper;

import com.inditex.code.prices.domain.dto.health.HealthStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HealthMapper {

    // Map from domain HealthStatus to API HealthStatus
    default com.inditex.code.prices.api.model.HealthStatus toApiModel(HealthStatus domainStatus) {
        if (domainStatus == null) {
            return null;
        }

        com.inditex.code.prices.api.model.HealthStatus apiStatus = new com.inditex.code.prices.api.model.HealthStatus();

        if (domainStatus.status() == HealthStatus.UP) {
            apiStatus.setStatus(com.inditex.code.prices.api.model.HealthStatus.StatusEnum.UP);
        } else {
            apiStatus.setStatus(com.inditex.code.prices.api.model.HealthStatus.StatusEnum.DOWN);
        }

        return apiStatus;
    }
}

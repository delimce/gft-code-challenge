package com.inditex.code.prices.domain.mapper;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.infrastructure.out.persistence.jpa.entity.PriceEntity;
import com.inditex.code.prices.api.model.PriceResponse;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PriceMapper {

    PriceDto toDto(PriceEntity entity);

    List<PriceResponseDto> toResponseDtoList(List<PriceEntity> entities);

    PriceResponseDto toResponseDto(PriceDto dto);

    List<PriceResponseDto> dtoListToResponseDtoList(List<PriceDto> dtos);

    // API model mappings
    PriceResponse toApiModel(PriceResponseDto dto);

    List<PriceResponse> toApiModelList(List<PriceResponseDto> dtos);

    PriceResponse toApiModelFromEntity(PriceEntity entity);

    List<PriceResponse> toApiModelListFromEntities(List<PriceEntity> entities);

    // Date conversion methods
    default OffsetDateTime map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atOffset(ZoneOffset.UTC);
    }

    default LocalDateTime map(OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
    }
}

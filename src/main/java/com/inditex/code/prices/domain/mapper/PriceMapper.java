package com.inditex.code.prices.domain.mapper;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.domain.dto.price.PriceResponseDto;
import com.inditex.code.prices.infrastructure.out.persistence.jpa.entity.PriceEntity;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PriceMapper {

    PriceDto toDto(PriceEntity entity);

    List<PriceResponseDto> toResponseDtoList(List<PriceEntity> entities);

    PriceResponseDto toResponseDto(PriceDto dto);

    List<PriceResponseDto> dtoListToResponseDtoList(List<PriceDto> dtos);
}

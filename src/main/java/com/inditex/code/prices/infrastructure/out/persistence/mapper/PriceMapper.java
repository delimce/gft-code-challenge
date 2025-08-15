package com.inditex.code.prices.infrastructure.out.persistence.mapper;

import com.inditex.code.prices.domain.dto.price.PriceDto;
import com.inditex.code.prices.infrastructure.out.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for converting between Price entities and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PriceMapper {

    PriceDto toDto(PriceEntity entity);

    List<PriceDto> toDtoList(List<PriceEntity> entities);

    PriceEntity toEntity(PriceDto dto);
}

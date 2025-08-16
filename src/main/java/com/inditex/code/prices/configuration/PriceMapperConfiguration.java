package com.inditex.code.prices.configuration;

import com.inditex.code.prices.domain.mapper.PriceMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit configuration to expose a {@link PriceMapper} bean.
 *
 * Although the MapStruct generated implementation is annotated with
 * {@code @Component},
 * Spring is currently not discovering it (likely due to annotation processing
 * or
 * incremental compilation quirks). Declaring the bean explicitly guarantees its
 * availability for constructor injection.
 */
@Configuration
public class PriceMapperConfiguration {

    @Bean
    public PriceMapper priceMapper() {
        return Mappers.getMapper(PriceMapper.class);
    }
}

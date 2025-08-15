package com.inditex.code.prices.infrastructure.out.persistence.repository;

import com.inditex.code.prices.infrastructure.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for Price entities.
 */
@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
}

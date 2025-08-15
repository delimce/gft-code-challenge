package com.inditex.code.prices.infrastructure.out.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inditex.code.prices.infrastructure.out.persistence.jpa.entity.PriceEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for Price entities.
 */
@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Find prices by filters: active date (between start and end dates), product
     * ID, and brand ID.
     * All parameters are optional - null values are ignored in the filtering.
     * When multiple prices exist for the same product, only the one with highest
     * priority is returned.
     * 
     * @param activeDate date when the product should be active for sale (between
     *                   start and end dates)
     * @param productId  ID of the product to filter by
     * @param brandId    ID of the brand to filter by
     * @return list of matching prices with highest priority per product
     */
    @Query("""
            SELECT p
            FROM PriceEntity p
            WHERE
                (:activeDate IS NULL OR (p.startDate <= :activeDate AND p.endDate >= :activeDate))
            AND (:productId IS NULL OR p.productId = :productId)
            AND (:brandId  IS NULL OR p.brandId  = :brandId)
            AND p.priority = (
                    SELECT MAX(p2.priority)
                    FROM PriceEntity p2
                    WHERE p2.productId = p.productId
                      AND p2.brandId   = p.brandId
                      AND (:activeDate IS NULL OR (p2.startDate <= :activeDate AND p2.endDate >= :activeDate))
                )
            ORDER BY p.productId, p.brandId, p.priority DESC
            """)
    List<PriceEntity> findByFilters(
            @Param("activeDate") LocalDateTime activeDate,
            @Param("productId") Long productId,
            @Param("brandId") Long brandId);
}

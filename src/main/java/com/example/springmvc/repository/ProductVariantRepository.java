package com.example.springmvc.repository;

import com.example.springmvc.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    boolean existsByProductIdAndSku(Long productId, String sku);
}

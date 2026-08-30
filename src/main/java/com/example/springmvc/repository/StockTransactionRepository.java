package com.example.springmvc.repository;

import com.example.springmvc.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findByProductVariantIdOrderByTimestampDesc(Long variantId);
}

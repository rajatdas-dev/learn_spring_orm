package com.example.springmvc.repository;

import com.example.springmvc.dto.response.inventory.InventoryResponseDTO;
import com.example.springmvc.entity.Inventory;
import com.example.springmvc.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductVariantId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.productVariant.id = :variantId")
    Optional<Inventory> findByProductVariantIdWithLock(@Param("variantId") Long variantId);

    List<InventoryResponseDTO> findAllById(Long id);

    boolean existsByProductVariantId(Long variantId);
}

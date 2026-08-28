package com.example.springmvc.repository;

import com.example.springmvc.dto.response.inventory.InventoryResponseDTO;
import com.example.springmvc.entity.Inventory;
import com.example.springmvc.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductVariantId(Long id);

    List<InventoryResponseDTO> findAllById(Long id);

    boolean existsByProductVariantId(Long variantId);
}

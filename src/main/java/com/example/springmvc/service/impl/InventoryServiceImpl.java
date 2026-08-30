package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.inventory.InventoryRequestDTO;
import com.example.springmvc.dto.response.inventory.InventoryResponseDTO;
import com.example.springmvc.entity.Inventory;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.InventoryAlreadyExistsException;
import com.example.springmvc.exception.ResourceNotFoundException;
import com.example.springmvc.entity.ProductVariant;
import com.example.springmvc.repository.InventoryRepository;
import com.example.springmvc.repository.ProductVariantRepository;
import com.example.springmvc.service.InventoryService;
import com.example.springmvc.util.mapper.ModelMapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;


    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public InventoryResponseDTO getInventories(Long id) {

        Inventory inventory = inventoryRepository
                .findByProductVariantId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.INVENTORY_NOT_FOUND,
                                "Inventory is not available !!"
                        )
                );

        return modelMapperUtil.map(inventory, InventoryResponseDTO.class);
    }

    @Override
    @Transactional
    public InventoryResponseDTO addInventory(
            Long variantId,
            InventoryRequestDTO inventoryRequestDTO
    ) {
        Inventory inventory = inventoryRepository
                .findByProductVariantId(variantId)
                .orElseGet(() -> {
                    com.example.springmvc.entity.ProductVariant variant = productVariantRepository.findById(variantId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                                    "Product variant not found with id: " + variantId
                            ));
                    Inventory newInventory = new Inventory();
                    newInventory.setProductVariant(variant);
                    newInventory.setQuantity(0);
                    newInventory.setReservedQuantity(0);
                    return newInventory;
                });

        inventory.setQuantity(
                inventory.getQuantity() + inventoryRequestDTO.getQuantity()
        );
        if (inventoryRequestDTO.getReservedQuantity() != null) {
            inventory.setReservedQuantity(inventoryRequestDTO.getReservedQuantity());
        }

        Inventory savedInventory = inventoryRepository.save(inventory);

        return modelMapperUtil.map(
                savedInventory,
                InventoryResponseDTO.class
        );
    }

    @Override
    public void deleteInventory(Long id) {

        Inventory inventory = inventoryRepository.findByProductVariantId(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.INVENTORY_NOT_FOUND,
                        "Inventory Not found at "+ id
                ));

        inventoryRepository.delete(inventory);
    }

    private void validateInventoryDoesNotExist(Long variantId) {

        if (inventoryRepository.existsByProductVariantId(variantId)) {
            throw new InventoryAlreadyExistsException("Inventory Already Exists");
        }
    }
}

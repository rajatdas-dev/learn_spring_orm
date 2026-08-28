package com.example.springmvc.service;

import com.example.springmvc.dto.request.inventory.InventoryRequestDTO;
import com.example.springmvc.dto.response.inventory.InventoryResponseDTO;
import com.example.springmvc.entity.ProductVariant;

import java.util.List;

public interface InventoryService {

    public InventoryResponseDTO getInventories(Long id);

    public InventoryResponseDTO addInventory(Long id, InventoryRequestDTO inventoryRequestDTO);

    public void deleteInventory(Long id);
}

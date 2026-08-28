package com.example.springmvc.controller;

import com.example.springmvc.dto.request.inventory.InventoryRequestDTO;
import com.example.springmvc.dto.response.inventory.InventoryResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/{variantId}")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> getInventories(
            @PathVariable("variantId") Long id
    ){

        InventoryResponseDTO inventoryResponseDTOS = inventoryService.getInventories(id);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Inventories successfully fetched !!",
                                inventoryResponseDTOS
                        )
                );
    }

    @PostMapping("/{variantId}/add")
    public ResponseEntity<ApiResponse<InventoryResponseDTO>> addInventory(
            @PathVariable Long variantId,
            @Valid @RequestBody InventoryRequestDTO inventoryRequestDTO
    ) {
        InventoryResponseDTO inventoryResponseDTO =
                inventoryService.addInventory(variantId, inventoryRequestDTO);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inventory successfully added !!",
                        inventoryResponseDTO
                )
        );
    }

    @PostMapping("/{variantId}/remove")
    public ResponseEntity<ApiResponse<Void>> removeInventory(@PathVariable Long variantId){

        inventoryService.deleteInventory(variantId);

        return  ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Inventory Successfully removed !!"
                        )
                );
    }

}

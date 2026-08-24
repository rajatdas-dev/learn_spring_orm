package com.example.springmvc.controller;

import com.example.springmvc.dto.request.vendor.VendorRequestDTO;
import com.example.springmvc.dto.response.VendorResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.VendorService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/create-vendor")
    public ResponseEntity<ApiResponse<VendorResponseDTO>> createVendor(@RequestBody VendorRequestDTO vendorRequestDTO){

        VendorResponseDTO vendorResponseDTO = vendorService.createVendor(vendorRequestDTO);

        return ResponseEntity.status(
                HttpStatus.CREATED).body(
                        ApiResponse.success(
                                "Vendor got successfully created",
                                vendorResponseDTO
                        )
        );

    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<VendorResponseDTO>>> getAllVendors(){

        List<VendorResponseDTO> vendorResponseDTOS = vendorService.getAllVendors();

        return  ResponseEntity.ok()
                .body(ApiResponse.success(
                        "All vendor's data got fetched",
                        vendorResponseDTOS
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDTO>> getVendorById(@PathVariable Long id){

        VendorResponseDTO vendorResponseDTO = vendorService.getById(id);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Vendor got fetched",
                                vendorResponseDTO
                        )
                );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDTO>> updateVendorEmailById(
            @PathVariable Long id,
            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Invalid email format")
            @RequestParam String email){
        VendorResponseDTO vendorResponseDTO = vendorService.updateEmailById(id, email);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Vendor updated successfully",
                                vendorResponseDTO
                        )
                );
    }
}

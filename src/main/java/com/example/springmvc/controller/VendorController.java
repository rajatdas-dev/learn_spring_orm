package com.example.springmvc.controller;

import com.example.springmvc.dto.request.VendorRequestDTO;
import com.example.springmvc.dto.response.VendorResponseDTO;
import com.example.springmvc.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/create-vendor")
    public ResponseEntity<VendorResponseDTO> createVendor(@RequestBody VendorRequestDTO vendorRequestDTO){

        return  ResponseEntity.ok(vendorService.createVendor(vendorRequestDTO));

    }
}

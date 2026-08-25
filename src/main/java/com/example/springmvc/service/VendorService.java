package com.example.springmvc.service;

import com.example.springmvc.dto.request.vendor.VendorRequestDTO;
import com.example.springmvc.dto.response.vendor.VendorResponseDTO;

import java.util.List;

public interface VendorService {

    public VendorResponseDTO createVendor(VendorRequestDTO vendorRequestDTO);

    public List<VendorResponseDTO> getAllVendors();

    public VendorResponseDTO getById(Long id);

    public VendorResponseDTO updateEmailById(Long id, String email);
}

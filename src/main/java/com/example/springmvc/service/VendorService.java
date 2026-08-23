package com.example.springmvc.service;

import com.example.springmvc.dto.request.VendorRequestDTO;
import com.example.springmvc.dto.response.VendorResponseDTO;

import java.util.List;

public interface VendorService {

    public VendorResponseDTO createVendor(VendorRequestDTO vendorRequestDTO);

    public List<VendorResponseDTO> getAllVendors();
}

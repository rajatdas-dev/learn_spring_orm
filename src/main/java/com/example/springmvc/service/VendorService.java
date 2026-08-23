package com.example.springmvc.service;

import com.example.springmvc.dto.request.VendorRequestDTO;
import com.example.springmvc.dto.response.VendorResponseDTO;

public interface VendorService {

    public VendorResponseDTO createVendor(VendorRequestDTO vendorRequestDTO);
}

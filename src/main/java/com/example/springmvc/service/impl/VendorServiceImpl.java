package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.VendorRequestDTO;
import com.example.springmvc.dto.response.VendorResponseDTO;
import com.example.springmvc.entity.Vendor;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.ResourceNotFoundException;
import com.example.springmvc.repository.VendorRepository;
import com.example.springmvc.service.VendorService;
import com.example.springmvc.util.mapper.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Override
    public VendorResponseDTO createVendor(VendorRequestDTO vendorRequestDTO) {

//        Vendor vendor = toEntity(vendorRequestDTO);
        Vendor vendor = modelMapperUtil.map(vendorRequestDTO, Vendor.class);
      Vendor savedVendor =   vendorRepository.save(vendor);

        return modelMapperUtil.map(savedVendor, VendorResponseDTO.class);
    }

    @Override
    public List<VendorResponseDTO> getAllVendors() {

        List<Vendor> vendorList = vendorRepository.findAll();
        return vendorList.stream()
                .map(vendor -> modelMapperUtil.map(vendor, VendorResponseDTO.class))
                .toList();
    }

    @Override
    public VendorResponseDTO getById(Long id) {

        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.VENDOR_NOT_FOUND,
                        "This vendor is not available"
                ));

        return modelMapperUtil.map(vendor, VendorResponseDTO.class);
    }
}

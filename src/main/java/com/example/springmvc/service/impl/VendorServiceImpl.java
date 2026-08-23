package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.VendorRequestDTO;
import com.example.springmvc.dto.response.VendorResponseDTO;
import com.example.springmvc.entity.Vendor;
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

        Vendor vendor = toEntity(vendorRequestDTO);
        vendorRepository.save(vendor);

        return toResponseDTO(vendor);
    }

    @Override
    public List<VendorResponseDTO> getAllVendors() {

        List<Vendor> vendorList = vendorRepository.findAll();
        return vendorList.stream()
                .map(vendor -> modelMapperUtil.map(vendor, VendorResponseDTO.class))
                .toList();
    }

    VendorResponseDTO toResponseDTO(Vendor vendor){

        VendorResponseDTO vendorResponseDTO = new VendorResponseDTO();

        vendorResponseDTO.setId(vendor.getId());
        vendorResponseDTO.setName(vendor.getName());
        vendorResponseDTO.setEmail(vendor.getEmail());

        return  vendorResponseDTO;
    }

    Vendor toEntity(VendorRequestDTO vendorRequestDTO){

        Vendor vendor = new Vendor();

        vendor.setName(vendorRequestDTO.getName());
        vendor.setEmail(vendorRequestDTO.getEmail());

        return  vendor;
    }
}

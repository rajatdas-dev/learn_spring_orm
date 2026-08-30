package com.example.springmvc.repository;

import com.example.springmvc.entity.VendorAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorAddressRepository extends JpaRepository<VendorAddress, Long> {
    Optional<VendorAddress> findByVendorId(Long vendorId);
}

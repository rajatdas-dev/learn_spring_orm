package com.example.springmvc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vendor_addresses")
public class VendorAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_address_seq")
    @SequenceGenerator(name = "vendor_address_seq", sequenceName = "vendor_address_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", unique = true, nullable = false)
    private Vendor vendor;
}

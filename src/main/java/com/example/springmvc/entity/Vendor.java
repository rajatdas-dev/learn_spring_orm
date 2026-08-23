package com.example.springmvc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vendors")
/// It lets you create object  using a clean, readable chain of methods instead of long, confusing constructor.
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_seq")
    @SequenceGenerator(name = "vendor_seq", sequenceName = "vendor_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "vendor")
    @Builder.Default
    ///  If you skip a field while using a Builder, Java usually forces it to be null or 0 - even if you
    /// gave it a default value in your code.
    ///  @Builder.Default forces the builder to respect your default value.
    private List<Product> products = new ArrayList<>();

}

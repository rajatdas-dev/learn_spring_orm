package com.example.springmvc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_variant_seq")
    @SequenceGenerator(name = "product_variant_seq", sequenceName = "product_var_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String sku;

    @Column(nullable = false)
    @NotNull
    private String color;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer size;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @OneToOne(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory inventory;
}

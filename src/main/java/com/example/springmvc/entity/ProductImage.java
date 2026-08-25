package com.example.springmvc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_img_seq")
    @SequenceGenerator(name = "product_img_seq",sequenceName = "product_image_sequence",allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String imageUrl;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer displayOrder;

    @NotNull
    @Column(nullable = false)
    private Boolean primaryImage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

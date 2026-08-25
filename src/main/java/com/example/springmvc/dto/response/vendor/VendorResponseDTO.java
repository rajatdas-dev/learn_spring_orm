package com.example.springmvc.dto.response.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorResponseDTO {

    private Long id;
    private String name;
    private String email;
}

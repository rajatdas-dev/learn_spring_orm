package com.example.springmvc.dto.request.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorRequestDTO {

    private String name;
    private String email;
}

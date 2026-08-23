package com.example.springmvc.dto.request;

import com.example.springmvc.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorRequestDTO {

    private String name;
    private String email;
}

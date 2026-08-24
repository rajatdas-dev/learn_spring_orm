package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.product.ProductRequestDTO;
import com.example.springmvc.dto.request.product.ProductUpdateRequestDTO;
import com.example.springmvc.dto.response.ProductResponseDTO;
import com.example.springmvc.entity.Category;
import com.example.springmvc.entity.Product;
import com.example.springmvc.entity.Vendor;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.ResourceNotFoundException;
import com.example.springmvc.repository.CategoryRepository;
import com.example.springmvc.repository.ProductRepository;
import com.example.springmvc.repository.VendorRepository;
import com.example.springmvc.service.ProductService;
import com.example.springmvc.util.mapper.ModelMapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO)
    {

        Vendor vendor = vendorRepository.findById(requestDTO.getVendorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.VENDOR_NOT_FOUND,
                                "Vendor not found")
                );

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ErrorCode.CATEGORY_NOT_FOUND,
                                "Category not found")
                );

        Product product = new Product();

        product.setName(requestDTO.getName());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setVendor(vendor);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {

        List<Product> productList = productRepository.findAll();

        return productList.stream()
                .map(product -> modelMapperUtil.map(product, ProductResponseDTO.class))
                .toList();
    }

    @Override
    public ProductResponseDTO getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "This product is not available"
                ));
        return modelMapperUtil.map(product, ProductResponseDTO.class);
    }

    @Transactional
    @Override
    public ProductResponseDTO updateProductsById(Long id, ProductUpdateRequestDTO productUpdateRequestDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "This product is not available"
                ));

        product.setName(productUpdateRequestDTO.getName());
        product.setPrice(productUpdateRequestDTO.getPrice());
        product.setStock(productUpdateRequestDTO.getStock());
        productRepository.flush();

        return  modelMapperUtil.map(product, ProductResponseDTO.class);
    }

    ProductResponseDTO toResponse(Product product){

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();

        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setVendorId(product.getVendor().getId());
        productResponseDTO.setVendorName(product.getVendor().getName());
        productResponseDTO.setStock(product.getStock());
        productResponseDTO.setCategoryId(product.getCategory().getId());
        productResponseDTO.setCategoryName(product.getCategory().getName());
//        productResponseDTO.setProducts(List.of(product));

        return productResponseDTO;
    }
}

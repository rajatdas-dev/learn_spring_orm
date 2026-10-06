package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.product.ProductRequestDTO;
import com.example.springmvc.dto.request.product.ProductUpdateRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantUpdateRequestDTO;
import com.example.springmvc.dto.response.product.ProductResponseDTO;
import com.example.springmvc.dto.response.product.ProductVariantResponseDTO;
import com.example.springmvc.entity.Category;
import com.example.springmvc.entity.Product;
import com.example.springmvc.entity.ProductVariant;
import com.example.springmvc.entity.Vendor;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.ProductOutOfStockException;
import com.example.springmvc.exception.ResourceNotFoundException;
import com.example.springmvc.repository.CategoryRepository;
import com.example.springmvc.repository.ProductRepository;
import com.example.springmvc.repository.ProductVariantRepository;
import com.example.springmvc.repository.VendorRepository;
import com.example.springmvc.service.ProductService;
import com.example.springmvc.util.mapper.ModelMapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private VendorRepository vendorRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private ProductVariantRepository productVariantRepository;

        @Autowired
        private ModelMapperUtil modelMapperUtil;

        @Transactional
        @Override
        public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

                Vendor vendor = vendorRepository.findById(requestDTO.getVendorId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorCode.VENDOR_NOT_FOUND,
                                                "Vendor not found"));

                Category category = categoryRepository.findById(requestDTO.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorCode.CATEGORY_NOT_FOUND,
                                                "Category not found"));

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

                // return productList.stream()
                // .map(product -> modelMapperUtil.map(product, ProductResponseDTO.class))
                // .toList();

                return productList.stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Override
        public ProductResponseDTO getById(Long id) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductOutOfStockException(
                                                "This product is not available"));
                return toResponse(product);
        }

        @Transactional
        @Override
        public ProductResponseDTO updateProductsById(Long id, ProductUpdateRequestDTO productUpdateRequestDTO) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductOutOfStockException(
                                                "This product is not available"));

                product.setName(productUpdateRequestDTO.getName());
                product.setPrice(productUpdateRequestDTO.getPrice());
                product.setStock(productUpdateRequestDTO.getStock());
                productRepository.flush();

                return toResponse(product);
        }

        @Transactional
        @Override
        public ProductVariantResponseDTO createProductVariant(Long id,
                        ProductVariantRequestDTO productVariantRequestDTO) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductOutOfStockException(
                                                "Product Not found"));

                if (productVariantRepository.existsByProductIdAndSku(
                                id,
                                productVariantRequestDTO.getSku())) {
                        throw new ResourceNotFoundException(
                                        ErrorCode.DUPLICATE_PRODUCT_VARIANT,
                                        "Product SKU already exists for this product");
                }

                ProductVariant productVariant = modelMapperUtil.map(productVariantRequestDTO, ProductVariant.class);

                productVariant.setProduct(product);
                product.getProductVariantList().add(productVariant);

                ProductVariant savedProductVariant = productVariantRepository.save(productVariant);

                return modelMapperUtil.map(savedProductVariant, ProductVariantResponseDTO.class);
        }

        @Override
        public ProductVariantResponseDTO getProductVariantById(Long id) {

                ProductVariant productVariant = productVariantRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                                                "This Product variant is not available"));

                return modelMapperUtil.map(productVariant, ProductVariantResponseDTO.class);
        }

        @Override
        public List<ProductVariantResponseDTO> getAllProductVariantsById(Long id) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductOutOfStockException(
                                                "Product Not Found !!"));

                List<ProductVariant> productVariantList = product.getProductVariantList();

                List<ProductVariantResponseDTO> productVariantResponseDTOS = productVariantList.stream()
                                .map((variant) -> modelMapperUtil.map(variant, ProductVariantResponseDTO.class))
                                .toList();

                return productVariantResponseDTOS;
        }

        @Transactional
        @Override
        public ProductVariantResponseDTO updateProductVariant(Long id,
                        ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO) {

                ProductVariant productVariant = productVariantRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                                                "Product Variant Not Found !!"));

                if (productVariantUpdateRequestDTO.getSku() != null) {
                        productVariant.setSku(productVariantUpdateRequestDTO.getSku());
                }

                if (productVariantUpdateRequestDTO.getColor() != null) {
                        productVariant.setColor(productVariantUpdateRequestDTO.getColor());
                }

                productVariant.setPrice(productVariantUpdateRequestDTO.getPrice());
                productVariant.setSize(productVariantUpdateRequestDTO.getSize());

                return modelMapperUtil.map(productVariant, ProductVariantResponseDTO.class);
        }

        @Override
        public void deleteProductVariant(Long id) {

                ProductVariant productVariant = productVariantRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                                                "Product Variant is not available"));

                productVariantRepository.delete(productVariant);
        }

        ProductResponseDTO toResponse(Product product) {

                ProductResponseDTO productResponseDTO = new ProductResponseDTO();

                productResponseDTO.setId(product.getId());
                productResponseDTO.setName(product.getName());
                productResponseDTO.setPrice(product.getPrice());
                productResponseDTO.setVendorId(product.getVendor().getId());
                productResponseDTO.setVendorName(product.getVendor().getName());
                productResponseDTO.setStock(product.getStock());
                productResponseDTO.setCategoryId(product.getCategory().getId());
                productResponseDTO.setCategoryName(product.getCategory().getName());

                List<ProductVariantResponseDTO> variants = product.getProductVariantList()
                                .stream()
                                .map(variant -> modelMapperUtil.map(
                                                variant,
                                                ProductVariantResponseDTO.class))
                                .toList();

                productResponseDTO.setProductVariants(variants);

                // productResponseDTO.setProducts(List.of(product));

                return productResponseDTO;
        }

        @Override
        public Page<ProductResponseDTO> getProductsPaged(Pageable pageable) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductsPaged'");
        }

        @Override
        public Page<ProductResponseDTO> searchProducts(String keyword, Long categoryId, Long vendorId,
                        BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, Pageable pageable) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'searchProducts'");
        }

        @Override
        public ProductResponseDTO getProductWithDetails(Long id) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductWithDetails'");
        }

        @Override
        public CompletableFuture<ProductResponseDTO> getProductAsync(Long id) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductAsync'");
        }

        @Override
        public List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getProductsByPriceRange'");
        }

        @Override
        public List<ProductResponseDTO> getLowStockProducts(Integer threshold) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getLowStockProducts'");
        }
}

package com.ecommerce.auth.service;

import com.ecommerce.auth.dto.request.CreateProductRequest;
import com.ecommerce.auth.dto.request.UpdateProductRequest;
import com.ecommerce.auth.dto.response.PageResponse;
import com.ecommerce.auth.dto.response.ProductResponse;
import com.ecommerce.auth.model.Category;
import com.ecommerce.auth.model.Product;
import com.ecommerce.auth.repository.CategoryRepository;
import com.ecommerce.auth.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String PRODUCT_CACHE_KEY = "product:";

//    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(
            int page, int size, String sortBy, String sortDir,
            String category, String search) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage;

        if (category != null && search != null) {
            productPage = productRepository.findByActiveTrueAndCategoryNameContainingIgnoreCaseAndNameContainingIgnoreCase(
                    category, search, pageable);
        } else if (category != null) {
            productPage = productRepository.findByActiveTrueAndCategoryNameContainingIgnoreCase(category, pageable);
        } else if (search != null) {
            productPage = productRepository.findByActiveTrueAndNameContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findByActiveTrue(pageable);
        }

        List<ProductResponse> products = productPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .content(products)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {
        String cacheKey = PRODUCT_CACHE_KEY + id;
        ProductResponse cachedProduct = (ProductResponse) redisTemplate.opsForValue().get(cacheKey);

        if (cachedProduct != null) {
            return cachedProduct;
        }

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductResponse response = convertToResponse(product);
        redisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(30));

        return response;
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .sku(request.getSku())
                .category(category)
                .active(true)
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .categoryName(product.getCategory().getName())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            product.setCategory(category);
        }

        // Update other fields
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getSku() != null) {
            product.setSku(request.getSku());
        }

        Product updatedProduct = productRepository.save(product);

        // Clear cache for this product
        String cacheKey = PRODUCT_CACHE_KEY + id;
        redisTemplate.delete(cacheKey);

        return convertToResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Soft delete - set active to false
        product.setActive(false);
        productRepository.save(product);

        // Clear cache for this product
        String cacheKey = PRODUCT_CACHE_KEY + id;
        redisTemplate.delete(cacheKey);
    }

}

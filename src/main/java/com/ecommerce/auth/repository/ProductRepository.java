package com.ecommerce.auth.repository;

import com.ecommerce.auth.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find active products with pagination
    Page<Product> findByActiveTrue(Pageable pageable);

    // Find active product by ID
    Optional<Product> findByIdAndActiveTrue(Long id);

    // Find products by category name (case-insensitive)
    @Query("SELECT p FROM Product p WHERE p.active = true AND LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%'))")
    Page<Product> findByActiveTrueAndCategoryNameContainingIgnoreCase(@Param("category") String category, Pageable pageable);

    // Find products by name search (case-insensitive)
    @Query("SELECT p FROM Product p WHERE p.active = true AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> findByActiveTrueAndNameContainingIgnoreCase(@Param("search") String search, Pageable pageable);

    // Find products by both category and name search (case-insensitive)
    @Query("SELECT p FROM Product p WHERE p.active = true AND LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%')) AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> findByActiveTrueAndCategoryNameContainingIgnoreCaseAndNameContainingIgnoreCase(
            @Param("category") String category,
            @Param("search") String search,
            Pageable pageable);

    // Find products by category ID
    Page<Product> findByActiveTrueAndCategoryId(Long categoryId, Pageable pageable);

    // Find products by SKU
    Optional<Product> findBySkuAndActiveTrue(String sku);

    // Find products with low stock
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stockQuantity <= :threshold")
    List<Product> findProductsWithLowStock(@Param("threshold") Integer threshold);

    // Find products by price range
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByActiveTrueAndPriceBetween(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

    // Find products by multiple categories
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.category.id IN :categoryIds")
    Page<Product> findByActiveTrueAndCategoryIdIn(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    // Count active products by category
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true AND p.category.id = :categoryId")
    Long countActiveProductsByCategory(@Param("categoryId") Long categoryId);

    // Find out of stock products
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.stockQuantity = 0")
    List<Product> findOutOfStockProducts();

    // Search products by name, description, or SKU
    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Check if SKU exists (for validation)
    boolean existsBySkuAndActiveTrue(String sku);

}

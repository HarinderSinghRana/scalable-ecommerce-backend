package com.ecommerce.auth.repository;

import com.ecommerce.auth.model.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find active categories
    List<Category> findByActiveTrue();

    // Find active categories with pagination
    Page<Category> findByActiveTrue(Pageable pageable);

    // Find active category by ID
    Optional<Category> findByIdAndActiveTrue(Long id);

    // Find category by name (case-insensitive)
    Optional<Category> findByNameIgnoreCaseAndActiveTrue(String name);

    // Find category by slug
    Optional<Category> findBySlugAndActiveTrue(String slug);

    // Find root categories (no parent)
    @Query("SELECT c FROM Category c WHERE c.active = true AND c.parent IS NULL ORDER BY c.sortOrder")
    List<Category> findRootCategories();

    // Find root categories with pagination
    @Query("SELECT c FROM Category c WHERE c.active = true AND c.parent IS NULL ORDER BY c.sortOrder")
    Page<Category> findRootCategories(Pageable pageable);

    // Find subcategories by parent ID
    @Query("SELECT c FROM Category c WHERE c.active = true AND c.parent.id = :parentId ORDER BY c.sortOrder")
    List<Category> findSubcategoriesByParentId(@Param("parentId") Long parentId);

    // Find categories by parent ID with pagination
    @Query("SELECT c FROM Category c WHERE c.active = true AND c.parent.id = :parentId ORDER BY c.sortOrder")
    Page<Category> findSubcategoriesByParentId(@Param("parentId") Long parentId, Pageable pageable);

    // Find categories by name search (case-insensitive)
    @Query("SELECT c FROM Category c WHERE c.active = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY c.sortOrder")
    List<Category> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    // Find categories by name search with pagination
    @Query("SELECT c FROM Category c WHERE c.active = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY c.sortOrder")
    Page<Category> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Check if category name exists (for validation)
    boolean existsByNameIgnoreCaseAndActiveTrue(String name);

    // Check if category slug exists (for validation)
    boolean existsBySlugAndActiveTrue(String slug);

    // Find categories with products count
    @Query("SELECT c, COUNT(p) FROM Category c LEFT JOIN c.products p WHERE c.active = true GROUP BY c ORDER BY c.sortOrder")
    List<Object[]> findCategoriesWithProductCount();

    // Find categories that have products
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p WHERE c.active = true AND p.active = true ORDER BY c.sortOrder")
    List<Category> findCategoriesWithProducts();

    // Find all categories in a hierarchy (category and its parents)
    @Query(value = "WITH RECURSIVE category_hierarchy AS (" +
            "SELECT c.id, c.name, c.slug, c.parent_id, c.active, 0 as level " +
            "FROM categories c WHERE c.id = :categoryId " +
            "UNION ALL " +
            "SELECT c.id, c.name, c.slug, c.parent_id, c.active, ch.level + 1 " +
            "FROM categories c JOIN category_hierarchy ch ON c.id = ch.parent_id) " +
            "SELECT * FROM category_hierarchy WHERE active = true", nativeQuery = true)
    List<Object[]> findCategoryHierarchy(@Param("categoryId") Long categoryId);

    // Find categories by level (depth in hierarchy)
    @Query("SELECT c FROM Category c WHERE c.active = true AND SIZE(c.subcategories) > 0 ORDER BY c.sortOrder")
    List<Category> findCategoriesWithSubcategories();

    // Find leaf categories (no subcategories)
    @Query("SELECT c FROM Category c WHERE c.active = true AND SIZE(c.subcategories) = 0 ORDER BY c.sortOrder")
    List<Category> findLeafCategories();

    // Find categories ordered by sort order
    List<Category> findByActiveTrueOrderBySortOrder();

    // Find categories by parent ordered by sort order
    List<Category> findByActiveTrueAndParentIdOrderBySortOrder(Long parentId);

    // Count subcategories
    @Query("SELECT COUNT(c) FROM Category c WHERE c.active = true AND c.parent.id = :parentId")
    Long countSubcategories(@Param("parentId") Long parentId);

    // Find categories that need to be updated (for maintenance)
    @Query("SELECT c FROM Category c WHERE c.active = true AND c.updatedAt < :threshold")
    List<Category> findCategoriesNeedingUpdate(@Param("threshold") java.time.LocalDateTime threshold);


}

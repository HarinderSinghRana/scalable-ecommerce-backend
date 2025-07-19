package com.ecommerce.auth.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product response data")
public class ProductResponse<T> {

    @Schema(description = "List of items in current page")
    private List<T> content;

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "iPhone 14 Pro")
    private String name;

    @Schema(description = "Product description", example = "Latest iPhone with advanced camera system")
    private String description;

    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;

    @Schema(description = "Product SKU", example = "IPHONE14PRO128")
    private String sku;

    @Schema(description = "Available stock quantity", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Product status", example = "ACTIVE")
    private String status; // ACTIVE, INACTIVE, OUT_OF_STOCK

    @Schema(description = "Product images")
    private List<String> imageUrls;

    @Schema(description = "Product weight in grams", example = "206")
    private Double weight;

    @Schema(description = "Product dimensions")
    private String dimensions;

    @Schema(description = "Product brand", example = "Apple")
    private String brand;

    @Schema(description = "Product model", example = "iPhone 14 Pro")
    private String model;

    @Schema(description = "Product color", example = "Deep Purple")
    private String color;

    @Schema(description = "Product size", example = "128GB")
    private String size;

    @Schema(description = "Product category")
    private CategoryResponse category;

    @Schema(description = "Category name", example = "Electronics")
    private String categoryName;

    @Schema(description = "Product active status", example = "true")
    private Boolean active;

    @Schema(description = "Product tags")
    private List<String> tags;

    @Schema(description = "Product rating", example = "4.5")
    private Double rating;

    @Schema(description = "Number of reviews", example = "124")
    private Integer reviewCount;

    @Schema(description = "Product creation date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Product last update date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}

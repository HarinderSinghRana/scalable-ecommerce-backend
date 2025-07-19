package com.ecommerce.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing product")
public class UpdateProductRequest {

    @Schema(description = "Product name", example = "iPhone 14 Pro")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;

    @Schema(description = "Product description", example = "Latest iPhone with advanced camera system")
    @Size(max = 2000, message = "Product description cannot exceed 2000 characters")
    private String description;

    @Schema(description = "Product price", example = "999.99")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Product price cannot exceed 999,999.99")
    private BigDecimal price;

    @Schema(description = "Product SKU", example = "IPHONE14PRO128")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "SKU can only contain uppercase letters, numbers, hyphens and underscores")
    private String sku;

    @Schema(description = "Available stock quantity", example = "50")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Max(value = 999999, message = "Stock quantity cannot exceed 999,999")
    private Integer stockQuantity;

    @Schema(description = "Category ID", example = "1")
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;

    @Schema(description = "Product weight in grams", example = "206")
    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
    @DecimalMax(value = "99999.99", message = "Weight cannot exceed 99,999.99 grams")
    private Double weight;

    @Schema(description = "Product dimensions (e.g., 15.7 x 7.65 x 0.79 cm)", example = "15.7 x 7.65 x 0.79 cm")
    @Size(max = 100, message = "Dimensions cannot exceed 100 characters")
    private String dimensions;

    @Schema(description = "Product brand", example = "Apple")
    @Size(max = 100, message = "Brand name cannot exceed 100 characters")
    private String brand;

    @Schema(description = "Product model", example = "iPhone 14 Pro")
    @Size(max = 100, message = "Model name cannot exceed 100 characters")
    private String model;

    @Schema(description = "Product color", example = "Deep Purple")
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    private String color;

    @Schema(description = "Product size", example = "128GB")
    @Size(max = 50, message = "Size cannot exceed 50 characters")
    private String size;

    @Schema(description = "Product image URLs")
    private List<@URL(message = "Invalid image URL") String> imageUrls;

    @Schema(description = "Product tags")
    private List<@Size(max = 50, message = "Tag cannot exceed 50 characters") String> tags;

    @Schema(description = "Product status (active/inactive)", example = "true")
    private Boolean active;

    @Schema(description = "SEO meta title", example = "iPhone 14 Pro - Latest Apple Smartphone")
    @Size(max = 200, message = "Meta title cannot exceed 200 characters")
    private String metaTitle;

    @Schema(description = "SEO meta description", example = "Discover the iPhone 14 Pro with advanced camera system and A16 Bionic chip")
    @Size(max = 500, message = "Meta description cannot exceed 500 characters")
    private String metaDescription;
}

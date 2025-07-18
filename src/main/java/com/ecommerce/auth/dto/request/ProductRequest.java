package com.ecommerce.auth.dto.request;

import com.ecommerce.auth.dto.response.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Product response data")
public class ProductRequest {

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

    public ProductRequest() {
    }

    public ProductRequest(Long id, String name, String description, BigDecimal price, String sku, Integer stockQuantity,
                          String status, List<String> imageUrls, Double weight, String dimensions, String brand,
                          String model, String color, String size, CategoryResponse category, List<String> tags,
                          Double rating, Integer reviewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.imageUrls = imageUrls;
        this.weight = weight;
        this.dimensions = dimensions;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.size = size;
        this.category = category;
        this.tags = tags;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //Getters & Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

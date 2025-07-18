package com.ecommerce.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category Response Schema")
public class CategoryResponse {

    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Electronics")
    private String name;

    @Schema(description = "Category description", example = "Electronic devices and accessories")
    private String description;

    @Schema(description = "Category slug", example = "electronics")
    private String slug;

    @Schema(description = "Category image URL")
    private String imageUrl;

    @Schema(description = "Parent category ID", example = "2")
    private Long parentId;

    @Schema(description = "Parent category name", example = "Technology")
    private String parentName;


    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, String description, String slug, String imageUrl, Long parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.parentId = parentId;
        this.parentName = parentName;
    }
}

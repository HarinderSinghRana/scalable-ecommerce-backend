package com.ecommerce.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = {"relationshipFields"}) // Exclude lazy/circular fields
@EqualsAndHashCode(exclude = {"relationshipFields"}) // Exclude collections
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, unique = true)
    private String slug;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Category> subcategories = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    private String imageUrl;

    private String metaTitle;

    @Column(length = 500)
    private String metaDescription;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    // Business methods
    public void addSubcategory(Category subcategory) {
        subcategories.add(subcategory);
        subcategory.setParent(this);
    }

    public void removeSubcategory(Category subcategory) {
        subcategories.remove(subcategory);
        subcategory.setParent(null);
    }

    public boolean isRootCategory() {
        return parent == null;
    }

    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }

    public boolean isLeafCategory() {
        return !hasSubcategories();
    }

    public int getLevel() {
        if (isRootCategory()) {
            return 0;
        }
        return parent.getLevel() + 1;
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

}

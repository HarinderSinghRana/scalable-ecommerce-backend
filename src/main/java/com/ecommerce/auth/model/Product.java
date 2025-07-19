package com.ecommerce.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = {"relationshipFields"}) // Exclude lazy/circular fields
@EqualsAndHashCode(exclude = {"relationshipFields"}) // Exclude collections
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private String sku;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default // Without this, builder will set fields to null instead of default values.
    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();


    //Product Builder
    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private String sku;
        private Category category;
        private Boolean active = true;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public ProductBuilder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public ProductBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public ProductBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.name = this.name;
            product.description = this.description;
            product.price = this.price;
            product.stockQuantity = this.stockQuantity;
            product.sku = this.sku;
            product.category = this.category;
            product.active = this.active;
            return product;
        }
    }
}

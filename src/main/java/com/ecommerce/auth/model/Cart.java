package com.ecommerce.auth.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer totalItems = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Constructors
    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    // Business methods
    public void addItem(Product product, Integer quantity) {
        CartItem existingItem = findCartItemByProduct(product);

        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this, product, quantity, product.getPrice());
            cartItems.add(newItem);
        }

        recalculateTotal();
    }

    public void removeItem(Product product) {
        CartItem itemToRemove = findCartItemByProduct(product);
        if (itemToRemove != null) {
            cartItems.remove(itemToRemove);
            recalculateTotal();
        }
    }

    public void updateItemQuantity(Product product, Integer newQuantity) {
        CartItem item = findCartItemByProduct(product);
        if (item != null) {
            if (newQuantity <= 0) {
                removeItem(product);
            } else {
                item.updateQuantity(newQuantity);
                recalculateTotal();
            }
        }
    }

    public void clearCart() {
        cartItems.clear();
        recalculateTotal();
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public boolean hasProduct(Product product) {
        return findCartItemByProduct(product) != null;
    }

    private CartItem findCartItemByProduct(Product product) {
        return cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    private void recalculateTotal() {
        this.totalAmount = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", totalAmount=" + totalAmount +
                ", totalItems=" + totalItems +
                ", itemCount=" + cartItems.size() +
                '}';
    }

}

package com.ecommerce.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name="orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = {"relationshipFields"}) // Exclude lazy/circular fields
@EqualsAndHashCode(exclude = {"relationshipFields"}) // Exclude collections
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingAmount;

    @Embedded
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "billing_street")),
            @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
            @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
            @AttributeOverride(name = "country", column = @Column(name = "billing_country")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip_code"))
    })
    private Address billingAddress;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private String paymentTransactionId;

    private LocalDateTime orderDate;

    private LocalDateTime shippedDate;

    private LocalDateTime deliveredDate;

    private String trackingNumber;

    @Column(length = 500)
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Business methods
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        recalculateTotal();
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.subtotal = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.taxAmount = subtotal.multiply(new BigDecimal("0.10")); // 10% tax
        this.totalAmount = subtotal.add(taxAmount).add(shippingAmount);
    }

    public void markAsShipped(String trackingNumber) {
        this.status = OrderStatus.SHIPPED;
        this.shippedDate = LocalDateTime.now();
        this.trackingNumber = trackingNumber;
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredDate = LocalDateTime.now();
    }

    public void cancel() {
        if (status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED) {
            this.status = OrderStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Cannot cancel order with status: " + status);
        }
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" +
                String.format("%04d", new Random().nextInt(10000));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderNumber, order.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", status=" + status +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                '}';
    }
}


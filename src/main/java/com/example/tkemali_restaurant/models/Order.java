package com.example.tkemali_restaurant.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", detailsSize=" + (orderDetails != null ? orderDetails.size() : 0) +
                '}';
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime orderDate;

    @Column(name = "customer_name")
    private String customerName;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();


    public Double calculateTotalPrice() {
        return orderDetails.stream()
                .mapToDouble(OrderDetail::getPrice)
                .sum();
    }

}

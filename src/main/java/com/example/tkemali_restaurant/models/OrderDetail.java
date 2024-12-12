package com.example.tkemali_restaurant.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetail {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "menu_item_id", nullable = false)
        private Menu menuItem;

        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        private Order order;

        private Integer quantity;

        @Transient
        private Double price; // Цена рассчитывается автоматически

        @Transient
        private Long menuItemId;


        public Double calculatePrice() {
                return menuItem.getPrice() * quantity;
        }
        public Double getPrice() {
                if (menuItem != null) {
                        return menuItem.getPrice() * quantity;
                }
                return 0.0;
        }



}

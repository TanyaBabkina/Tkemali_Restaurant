package com.example.tkemali_restaurant.models;

import jakarta.persistence.*;

@Entity
@Table(name = "event_planning")
public class EventPlanning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Transient
    private Long menuId;

    private int quantity;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getMenuId() {
        // Возвращает menuId, если оно установлено, или значение ID из menu
        return menuId != null ? menuId : (menu != null ? menu.getId() : null);
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}

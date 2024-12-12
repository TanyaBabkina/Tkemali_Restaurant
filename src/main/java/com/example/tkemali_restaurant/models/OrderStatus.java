package com.example.tkemali_restaurant.models;

public enum OrderStatus {
    В_ОЖИДАНИИ("В ожидании"),
    ОТМЕНЕН("Отменён"),
    ВЫПОЛНЯЕТСЯ("Выполняется"),
    ЗАВЕРШЕН("Завершен");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }



    public static OrderStatus fromString(String text) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет значения Enum для: " + text);
    }
}

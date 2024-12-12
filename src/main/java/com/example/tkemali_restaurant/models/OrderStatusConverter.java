package com.example.tkemali_restaurant.models;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        return attribute != null ? attribute.getDisplayName() : null;
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? OrderStatus.fromString(dbData) : null;
    }
}

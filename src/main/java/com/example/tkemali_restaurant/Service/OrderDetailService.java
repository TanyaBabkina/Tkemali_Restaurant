package com.example.tkemali_restaurant.Service;

import com.example.tkemali_restaurant.models.OrderDetail;
import com.example.tkemali_restaurant.Repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(Math.toIntExact(id));
    }
}

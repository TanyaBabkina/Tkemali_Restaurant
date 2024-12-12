package com.example.tkemali_restaurant.Service;

import com.example.tkemali_restaurant.models.Order;
import com.example.tkemali_restaurant.models.OrderDetail;
import com.example.tkemali_restaurant.models.OrderStatus;
import com.example.tkemali_restaurant.Repository.OrderDetailRepository;
import com.example.tkemali_restaurant.Repository.OrderRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    public OrderDetailRepository orderDetailRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

//    @Autowired
//    public OrderService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
@Transactional
public void saveOrderWithDetails(Order order) {
    orderRepository.save(order);
    for (OrderDetail detail : order.getOrderDetails()) {
        detail.setOrder(order);
        orderDetailRepository.save(detail);
    }
}
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
        for (OrderDetail detail : order.getOrderDetails()) {
            detail.setOrder(order);
            orderDetailRepository.save(detail);
        }
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getAllOrdersWithDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> ordersPage = orderRepository.findAllWithDetails(pageable);
        return ordersPage.getContent();
    }


    public Optional<Order> getOrderWithDetails(Long id) {
        return orderRepository.findByIdWithDetails(id);
    }

    public List<OrderDetail> getOrderDetails(Long orderId) {

        return orderDetailRepository.findByOrderId(Math.toIntExact(orderId));
    }
    public List<OrderDetail> getOrderDetailsByOrderId(int id) {
        return orderDetailRepository.findByOrderId(id);
    }
    public Order getOrderWithDetailsAndMenu(Long orderId) {
        return orderRepository.findOrderWithDetailsAndMenu(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Page<Order> getOrdersWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }
    public int getTotalPages(int size) {
        long totalOrders = orderRepository.count(); // Получаем общее количество записей
        return (int) Math.ceil((double) totalOrders / size); // Рассчитываем количество страниц
    }
    public Map<String, Long> getOrderStatusCounts() {
        return (Map<String, Long>) orderRepository.countOrdersByStatus();
    }

//    public Map<String, Long> countOrdersByStatus() {
//        List<Object[]> results = orderRepository.countOrdersByStatusRaw();
//        return results.stream()
//                .collect(Collectors.toMap(
//                        result -> (String) result[0], // Статус заказа
//                        result -> (Long) result[1]   // Количество заказов
//                ));
//    }


    public Page<Order> findAllPaginated(PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }
    public Map<OrderStatus, Long> countOrdersByStatus() {
        List<Object[]> results = orderRepository.countOrdersByStatus();
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (OrderStatus) result[0], // Преобразуем статус обратно в Enum
                        result -> (Long) result[1] // Количество заказов
                ));
    }
}

package com.example.tkemali_restaurant.Repository;

import com.example.tkemali_restaurant.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Метод для получения всех заказов с деталями с пагинацией
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails")
    Page<Order> findAllWithDetails(Pageable pageable);

    // Метод для получения одного заказа с деталями
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    // Метод для получения одного заказа с деталями и элементами меню
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.menuItem WHERE o.id = :id")
    Optional<Order> findOrderWithDetailsAndMenu(@Param("id") Long id);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    Order findUniqueOrderByStatus(@Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.status = :status")
    List<Order> findOrdersByStatus(@Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.orderDate DESC")
    Order findLatestOrderByStatus(@Param("status") String status);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatusRaw();
}

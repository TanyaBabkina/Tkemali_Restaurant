package com.example.tkemali_restaurant.Controllers;

import com.example.tkemali_restaurant.models.Menu;
import com.example.tkemali_restaurant.models.Order;
import com.example.tkemali_restaurant.models.OrderDetail;
import com.example.tkemali_restaurant.Repository.OrderRepository;
import com.example.tkemali_restaurant.Service.MenuService;
import com.example.tkemali_restaurant.Service.OrderDetailService;
import com.example.tkemali_restaurant.Service.OrderService;
//import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.tkemali_restaurant.models.OrderStatus;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("home/orders")
public class OrderController {

    private final OrderService orderService;
    private final MenuService menuService;
    private final OrderDetailService orderDetailService;
    private final OrderRepository orderRepository;
//    private static final Logger log = (Logger) LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService, MenuService menuService, OrderDetailService orderDetailService, OrderRepository orderRepository) {
        this.orderService = orderService;

        this.menuService = menuService;
        this.orderDetailService = orderDetailService;
        this.orderRepository = orderRepository;
    }


    @GetMapping
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, HttpServletRequest request
    ) {
        // Ensure the page number is within valid bounds
        if (page < 0) {
            page = 0;
        }

        Page<Order> orders = orderService.findAllPaginated(PageRequest.of(page, size));
        Map<OrderStatus, Long> orderStatusData = orderService.countOrdersByStatus();

        model.addAttribute("currentPath", request.getRequestURI());
        model.addAttribute("orders", orders.getContent());
        model.addAttribute("totalPages", orders.getTotalPages());
        model.addAttribute("currentPage", page); // Add currentPage to the model
        model.addAttribute("orderStatusData", orderStatusData);

        return "orders/list";
    }



    @GetMapping("/add")
    public String showAddOrderForm(Model model) {
        model.addAttribute("menuItems", menuService.getAllMenuItems()); // Передаем список блюд
        model.addAttribute("order", new Order()); // Создаем новый объект заказа
        return "orders/add"; // Указываем шаблон
    }

@PostMapping("/add")
public String addOrder(@Valid @ModelAttribute Order order, BindingResult result, Model model) {
    if (result.hasErrors()) {
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "orders/add";
    }

    try {
        for (OrderDetail detail : order.getOrderDetails()) {
            if (detail.getMenuItemId() != null) {
                Menu menuItem = menuService.getMenuById(detail.getMenuItemId());
                detail.setMenuItem(menuItem);
                detail.setPrice(detail.getQuantity() * menuItem.getPrice()); // Рассчитываем цену
                detail.setOrder(order);
            } else {
                throw new IllegalArgumentException("MenuItemId cannot be null");
            }
        }

        order.setTotalPrice(order.calculateTotalPrice()); // Вычисляем итоговую цену заказа
        orderService.saveOrder(order);
        return "redirect:/home/orders";
    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/home/orders/add?error=true";
    }
}

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/home/orders";
    }


    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/home/orders?error=true";
        }

        model.addAttribute("order", order);
        model.addAttribute("menuItems", menuService.getAllMenuItems()); // Убедитесь, что данные передаются
        return "orders/edit";
    }


    @PostMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, @Valid @ModelAttribute Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            return "orders/edit";
        }

        try {
            Order existingOrder = orderService.getOrderById(id);
            if (existingOrder == null) {
                return "redirect:/home/orders?error=true";
            }

            // Обновление основных данных заказа
            existingOrder.setCustomerName(order.getCustomerName());
            existingOrder.setOrderDate(order.getOrderDate());
            existingOrder.setStatus(order.getStatus());

            // Сопоставление новых деталей заказа с существующими
            List<OrderDetail> existingDetails = existingOrder.getOrderDetails();

            // Удаление старых деталей, которых нет в новом заказе
            existingDetails.removeIf(detail ->
                    order.getOrderDetails().stream()
                            .noneMatch(newDetail -> newDetail.getId() != null && newDetail.getId().equals(detail.getId()))
            );

            // Добавление или обновление существующих деталей
            for (OrderDetail newDetail : order.getOrderDetails()) {
                if (newDetail.getId() == null) {
                    // Новый элемент
                    Menu menuItem = menuService.getMenuById(newDetail.getMenuItemId());
                    newDetail.setMenuItem(menuItem);
                    newDetail.setPrice(menuItem.getPrice() * newDetail.getQuantity());
                    newDetail.setOrder(existingOrder);
                    existingDetails.add(newDetail);
                } else {
                    // Обновление существующего элемента
                    OrderDetail existingDetail = existingDetails.stream()
                            .filter(detail -> detail.getId().equals(newDetail.getId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Invalid detail ID: " + newDetail.getId()));
                    existingDetail.setQuantity(newDetail.getQuantity());
                    existingDetail.setPrice(existingDetail.getMenuItem().getPrice() * newDetail.getQuantity());
                }
            }

            existingOrder.setTotalPrice(existingOrder.calculateTotalPrice());
            orderService.saveOrder(existingOrder);

            return "redirect:/home/orders";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("menuItems", menuService.getAllMenuItems());
            return "orders/edit?error=true";
        }
    }





    @GetMapping("/{id}/details")
    @ResponseBody
    public List<Map<String, Object>> getOrderDetails(@PathVariable Long id) {
        Order order = orderService.getOrderWithDetailsAndMenu(id);

        return order.getOrderDetails().stream()
                .map(detail -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("menuItemName", detail.getMenuItem().getName());
                    map.put("quantity", detail.getQuantity());
                    map.put("price", detail.getPrice());
                    return map;
                })
                .collect(Collectors.toList());
    }
    @GetMapping("/order-status-data")
    @ResponseBody
    public Map<OrderStatus, Long> getOrderStatusData() {
        return orderService.countOrdersByStatus();
    }
}

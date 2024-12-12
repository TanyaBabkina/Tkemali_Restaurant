

package com.example.tkemali_restaurant.Controllers;

import com.example.tkemali_restaurant.models.Category;
import com.example.tkemali_restaurant.models.Menu;
import com.example.tkemali_restaurant.Repository.MenuRepository;
import com.example.tkemali_restaurant.Service.CategoryService;
import com.example.tkemali_restaurant.Service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("home/menu")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @GetMapping
    public String menu(Model model, HttpServletRequest request) {
        List<Menu> menuItems = menuRepository.findAll();
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("currentPath", request.getRequestURI());
        model.addAttribute("categories", categoryService.getAllCategories()); // Добавляем категории

        return "menu/menu";
    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Menu());
        model.addAttribute("categories", menuService.getAllCategories()); // Ensure this returns a non-null list

        return "menu/add";
    }

    @PostMapping("/add")
    public String addMenuItem(@ModelAttribute Menu product,
                              @RequestParam("category") Long categoryId,
                              @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (categoryId == null) {
                return "redirect:/home/menu/add?error=CategoryNotSelected"; // Redirect with error message
            }

            Category category = menuService.getCategoryById(categoryId);
            if (category == null) {
                logger.error("Category with id {} not found", categoryId);
                return "redirect:/home/menu/add?error=CategoryNotFound";
            }

            product.setCategory(category); // Set the selected category
            menuService.saveProductWithImage(product, imageFile);
        } catch (IOException e) {
            logger.error("Error saving menu item: {}", e.getMessage());
            e.printStackTrace();
            return "error"; // Return error page
        }
        return "redirect:/home/menu"; // Redirect to menu page
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Menu product = menuService.getProductById(id);
        if (product == null) {
            logger.error("Menu item with id {} not found", id);
            return "redirect:/home/menu?error=MenuItemNotFound";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", menuService.getAllCategories()); // Add categories to the model for editing
        return "menu/edit"; // Return edit form
    }

    @PostMapping("/edit/{id}")
    public String editMenuItem(@PathVariable Long id,
                               @ModelAttribute Menu product,
                               @RequestParam("category") Long categoryId,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            logger.info("Editing menu item with id: {}", id);
            logger.info("Category ID: {}", categoryId);

            // Проверка наличия категории
            Category category = menuService.getCategoryById(categoryId);
            if (category == null) {
                logger.error("Category with id {} not found", categoryId);
                return "redirect:/home/menu/edit/" + id + "?error=CategoryNotFound";
            }

            // Проверка наличия продукта
            Menu existingProduct = menuService.getProductById(id);
            if (existingProduct == null) {
                logger.error("Menu item with id {} not found", id);
                return "redirect:/home/menu?error=MenuItemNotFound";
            }
            // Обновление продукта
            product.setCategory(category);
            menuService.updateProductWithImage(id, product, imageFile);

            logger.info("Menu item with id {} updated successfully", id);
        } catch (IOException e) {
            logger.error("Error updating menu item with id {}: {}", id, e.getMessage());
            e.printStackTrace();
            return "error"; // Return error page
        }
        return "redirect:/home/menu"; // Redirect to menu page after editing
    }

    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuService.deleteProduct(id);
        return "redirect:/home/menu"; // Перенаправляем на страницу меню после удаления
    }



}


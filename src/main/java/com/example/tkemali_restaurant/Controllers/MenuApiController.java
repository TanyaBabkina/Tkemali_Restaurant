package com.example.tkemali_restaurant.Controllers;

import com.example.tkemali_restaurant.models.Menu;
import com.example.tkemali_restaurant.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("home/load/menu")
public class MenuApiController {

    private final MenuService menuService;

    @Autowired
    public MenuApiController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Menu> getAllMenuItems() {
        return menuService.getAllMenuItems();
    }

    @PostMapping
    public ResponseEntity<String> addMenuItem(@RequestPart("product") Menu product, @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            menuService.saveProductWithImage(product, imageFile);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMenuItem(@PathVariable Long id, @RequestPart("product") Menu product, @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            menuService.updateProductWithImage(id, product, imageFile);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        try {
            menuService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuItemById(@PathVariable Long id) {
        Menu menu = menuService.getProductById(id);
        if (menu != null) {
            return ResponseEntity.ok(menu);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/search")
    public ResponseEntity<List<Menu>> searchMenu(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double price) {
        List<Menu> filteredMenu = menuService.searchMenu(name, category, price);
        return ResponseEntity.ok(filteredMenu);
    }

}

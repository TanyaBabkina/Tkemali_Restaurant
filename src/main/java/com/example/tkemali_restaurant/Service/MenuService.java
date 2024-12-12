

package com.example.tkemali_restaurant.Service;
import com.example.tkemali_restaurant.models.Category;
import com.example.tkemali_restaurant.models.Menu;
import com.example.tkemali_restaurant.Repository.CategoryRepository;
import com.example.tkemali_restaurant.Repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    private final String uploadDir; // Store the upload directory
    private final MenuRepository menuRepository; // Store the menu repository

    @Autowired
    public MenuService(@Value("${upload.dir}") String uploadDir, MenuRepository menuRepository) {
        this.uploadDir = uploadDir; // Initialize uploadDir from properties
        this.menuRepository = menuRepository; // Initialize menuRepository
        createUploadDirIfNotExists(); // Create directory if it doesn't exist
        logger.debug("MenuService initialized with uploadDir: {}", uploadDir);
    }
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public List<Menu> getAllMenuItems() {
        return menuRepository.findAll();
    }



    public void saveProductWithImage(Menu product, MultipartFile imageFile) throws IOException {
        product.setImageVersion(0); // Set initial image version

        Menu savedProduct = menuRepository.save(product); // Save product to database

        if (!imageFile.isEmpty()) { // Check if the image file is not empty
            saveImage(savedProduct, imageFile); // Save the image
        }
    }


    public void deleteProduct(Long id) {
        if (menuRepository.existsById(id)) {
            Menu product = menuRepository.findById(id).orElse(null);
            deleteImage(product.getImagePath()); // Delete associated image
            menuRepository.deleteById(id); // Delete product from database
        } else {
            throw new RuntimeException("Продукт не найден");
        }
    }

    public Menu getProductById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }


private void saveImage(Menu product, MultipartFile imageFile) throws IOException {
    try {
        String fileName = product.getId() + "_" + System.currentTimeMillis() + ".jpg"; // Генерация имени файла
        Path uploadPath = Paths.get(uploadDir);

        if (Files.notExists(uploadPath)) { // Создание директории, если она не существует
            Files.createDirectories(uploadPath);
        }

        Path imagePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        product.setImagePath(fileName); // Сохраняем только имя файла в объекте продукта
        menuRepository.save(product); // Обновляем продукт в базе данных
    } catch (IOException e) {
        logger.error("Ошибка при сохранении изображения", e);
        throw new RuntimeException("Ошибка при сохранении изображения", e);
    }
}
    public void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Path path = Paths.get(imagePath);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
                // Логирование ошибки
            }
        }
    }

    public void updateProductWithImage(Long id, Menu product, MultipartFile imageFile) throws IOException {
        Menu existingProduct = menuRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Menu item with id " + id + " not found");
        }

        // Обновление полей продукта
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());

        // Удаление старого изображения, если новое изображение загружено
        if (imageFile != null && !imageFile.isEmpty()) {
            String oldImagePath = existingProduct.getImagePath();
            if (oldImagePath != null && !oldImagePath.isEmpty()) {
                deleteImage(oldImagePath);
            }
            // Сохранение нового изображения
            saveImage(existingProduct, imageFile); // Используем новую функцию для сохранения изображения
        }

        // Сохранение обновленного продукта
        menuRepository.save(existingProduct);
    }


    private void createUploadDirIfNotExists() {
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new RuntimeException("Путь к папке с изображениями не указан");
        }

        Path uploadPath = Paths.get(uploadDir);

        if (Files.notExists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                logger.info("Директория для загрузки изображений создана: {}", uploadPath.toAbsolutePath());
            } catch (IOException e) {
                logger.error("Не удалось создать директорию для загрузки изображений", e);
                throw new RuntimeException("Не удалось создать директорию для загрузки изображений", e);
            }
        }
    }

    public boolean productExists(Long id) {
        return menuRepository.existsById(id);
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        return categoryOptional.orElse(null);
    }


    public Menu getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блюдо с ID " + id + " не найдено"));
    }
    public List<Menu> searchMenu(String name, String category, Double price) {
        return menuRepository.findAll().stream()
                .filter(menu -> (name == null || name.trim().isEmpty() || menu.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(menu -> (category == null || category.trim().isEmpty() || menu.getCategory().getName().equalsIgnoreCase(category)))
                .filter(menu -> (price == null || menu.getPrice() <= price))
                .collect(Collectors.toList());
    }



}
package com.example.tkemali_restaurant.Controllers;
import com.example.tkemali_restaurant.models.Menu;
import com.example.tkemali_restaurant.Repository.MenuRepository;
import com.example.tkemali_restaurant.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {


    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @GetMapping("/home/about")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String about(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "about";  // Возвращаем Thymeleaf-шаблон about.html
    }

    @GetMapping("/home/profile")
    public String profile(Model model) {
        model.addAttribute("currentPath", "profile");
        return "profile";
    }


}


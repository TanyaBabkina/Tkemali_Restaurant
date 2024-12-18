package com.example.tkemali_restaurant.Controllers;

import com.example.tkemali_restaurant.exception.EmailAlreadyExistsException;
import com.example.tkemali_restaurant.models.PasswordUpdateRequest;
import com.example.tkemali_restaurant.models.User;
import com.example.tkemali_restaurant.models.UserRole;
import com.example.tkemali_restaurant.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/home/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public String getUserProfile(@PathVariable String email, Model model, HttpServletRequest request) {
        User user = userService.getUserByEmail(email);
        System.out.println(user);
        if (user == null) {
            return "redirect:/error"; // Handle not found case
        }
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentPath", request.getRequestURI());
        return "profile"; // Return the Thymeleaf template name
    }

    @PutMapping("/{email}")
    public ResponseEntity<Void> updateUserProfile(@PathVariable String email, @RequestBody User user) {
        userService.updateUserByEmail(user.getEmail(), user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable String email, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        System.out.println(email);
        userService.updatePasswordByEmail(email, passwordUpdateRequest.getUserNewPassword());
        return ResponseEntity.noContent().build();
    }


@PostMapping("/add")
public ResponseEntity<?> addUser(@RequestBody User user) {
    try {

        userService.registerUser(user);
        return ResponseEntity.ok().build(); // Успешное добавление
    } catch (EmailAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", e.getMessage()));
    }
}



    @PostMapping("/update/{id}")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestParam UserRole role) {
        userService.updateUserRole(id, role);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Delete the user by ID
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", e.getMessage()));
        }
    }






}
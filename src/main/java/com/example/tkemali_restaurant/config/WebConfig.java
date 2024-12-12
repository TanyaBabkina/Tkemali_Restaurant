package com.example.tkemali_restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Adjust this path as needed to match your upload directory structure.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+uploadDir); // Ensure this matches your upload directory
    }
}
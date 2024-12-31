package com.qjewels.qjewels.utils.config.general;

import com.qjewels.qjewels.service.IImageService;
import com.qjewels.qjewels.service.implementation.ImageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public IImageService imageService() {
        return new ImageService();
    }
}
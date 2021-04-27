package com.EGEA1R.CarService.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
            //    .allowedOrigins("http://localhost:81")
             //   .allowedOrigins("http://84.2.172.134:4200")
                .allowedMethods("GET", "POST", "DELETE", "PUT");
    }
}

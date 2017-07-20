package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyWebMvcConfig {

    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {

            // This is required as otherwise it wont forward to index.html under /docs
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/docs").setViewName(
                        "forward:/docs/index.html");
            }
        };
    }

}
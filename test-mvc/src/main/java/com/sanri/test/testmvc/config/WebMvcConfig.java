package com.sanri.test.testmvc.config;

import com.sanri.test.testmvc.service.HelloBiz;
import com.sanri.test.testmvc.service.StrategyHelloBizMethodArgumentResolver;
import com.sanri.test.testmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private Map<String, HelloBiz> helloBizMap = new HashMap<>();

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        ConfigMethodArgumentResolver configMethodArgumentResolver = new ConfigMethodArgumentResolver();
        configMethodArgumentResolver.setUserService(userService);
        argumentResolvers.add(configMethodArgumentResolver);

        StrategyHelloBizMethodArgumentResolver strategyHelloBizMethodArgumentResolverr = new StrategyHelloBizMethodArgumentResolver(helloBizMap);
        argumentResolvers.add(strategyHelloBizMethodArgumentResolverr);
    }
}
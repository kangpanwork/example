package com.sanri.test.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 091795960
 */

@SpringBootApplication
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.sanri.test.jetcache.service")
public class JetCacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(JetCacheApplication.class,args);
    }
}

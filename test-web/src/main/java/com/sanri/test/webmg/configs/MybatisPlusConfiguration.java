package com.sanri.test.webmg.configs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.sanri.test.webmg.mapper")
public class MybatisPlusConfiguration {
}

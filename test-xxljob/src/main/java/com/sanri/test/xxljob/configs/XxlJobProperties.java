package com.sanri.test.xxljob.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "xxl.job")
@Data
@Component
public class XxlJobProperties {
    private String accessToken;
    private Admin admin;
    private Executor executor;

    @Data
    public static class Admin{
        private String addresses;
    }

    @Data
    public static class Executor{
        private String address;
        private String appname;
        private String ip;
        private String logpath;
        private Integer logretentiondays;
        private Integer port;
    }
}

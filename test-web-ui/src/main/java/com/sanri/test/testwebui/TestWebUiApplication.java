package com.sanri.test.testwebui;

import com.sanri.web.bigfile.EnableBigFileUpload;
import com.sanri.web.configs.EnableWebUI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableWebUI
public class TestWebUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestWebUiApplication.class, args);
    }

}

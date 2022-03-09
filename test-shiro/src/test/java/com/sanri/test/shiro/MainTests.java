package com.sanri.test.shiro;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MainTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testEncode(){
        for (int i = 0; i < 10; i++) {
            String admin = passwordEncoder.encode("admin");

            boolean matches = passwordEncoder.matches("admin", admin);
            System.out.println(admin+" == "+ matches);
        }
    }
}

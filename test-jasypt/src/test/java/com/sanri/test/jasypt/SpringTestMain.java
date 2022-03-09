package com.sanri.test.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringTestMain {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void decrypt(){
        String decrypt = stringEncryptor.decrypt("0pnIsNHSKFDA/1aHfGIoHg==");
        System.out.println(decrypt);
    }

    @Test
    public void encrypt(){
        System.out.println(stringEncryptor.encrypt("lx520"));
    }
}

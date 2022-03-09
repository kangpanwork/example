package com.sanri.test.cycle2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext("com.sanri.test.cycle2");
        A bean = annotationConfigApplicationContext.getBean(A.class);
        System.out.println(bean);
    }
}

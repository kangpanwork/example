package com.sanri.test.testmvc.proxy;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

public class AnnoTest {
    public static void main(String[] args) throws NoSuchMethodException {
        Class<InterfaceTarget> interfaceTargetClass = InterfaceTarget.class;
        Method queryByName = interfaceTargetClass.getMethod("queryByName",String.class);

        RequestMapping mergedAnnotation = AnnotatedElementUtils.getMergedAnnotation(queryByName, RequestMapping.class);
        System.out.println(mergedAnnotation);

//        GetMapping getMapping = AnnotationUtils.findAnnotation(queryByName, GetMapping.class);
//        PostMapping postMapping = AnnotationUtils.findAnnotation(queryByName, PostMapping.class);
//        RequestMapping requestMapping = AnnotationUtils.findAnnotation(queryByName, RequestMapping.class);
//
//        GetMapping getMappingGet = AnnotationUtils.getAnnotation(queryByName, GetMapping.class);
//        PostMapping postMappingGet = AnnotationUtils.getAnnotation(queryByName, PostMapping.class);
//        RequestMapping requestMappingGet = AnnotationUtils.getAnnotation(queryByName, RequestMapping.class);
//
//
//        Annotation[] annotations = AnnotationUtils.getAnnotations(queryByName);
//        System.out.println(annotations);
//
//
//        System.out.println(getMapping);
//        System.out.println(postMapping);
//        System.out.println(requestMapping);
//        System.out.println("-----------------------------------------------------");
//        System.out.println(getMappingGet);
//        System.out.println(postMappingGet);
//        System.out.println(requestMappingGet);
    }
}

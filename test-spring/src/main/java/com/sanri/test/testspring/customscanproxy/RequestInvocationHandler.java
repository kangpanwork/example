package com.sanri.test.testspring.customscanproxy;

import com.sanri.test.testspring.customscanproxy.beansdef.Request;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class RequestInvocationHandler implements InvocationHandler {

    ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("{",  "}",":",true);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            log.info("当前是 Object 中的方法调用 " + method);
            return method.invoke(this, args);
        }
        Class<?> returnType = method.getReturnType();
        Class<?>[] parameterTypes = method.getParameterTypes();

        Class<?> declaringClass = method.getDeclaringClass();
        Request rpcClient = AnnotationUtils.findAnnotation(declaringClass, Request.class);

        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        GetMapping requestMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
        RequestMethod[] requestMethods = {RequestMethod.GET};
        String[] paths = requestMapping.value();
        String[] consumes = requestMapping.consumes();

//        RestTemplate restTemplate = new RestTemplate();
        String path = paths[0];
        Properties properties = new Properties();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            String parameterName = parameterNames[i];
            RequestParam requestParam = AnnotationUtils.getAnnotation(parameterType, RequestParam.class);
            if (requestParam != null) {
                parameterName = requestParam.name();
            }

            Object arg = args[i];
            if (ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType == String.class) {
                properties.put(parameterName, arg);
            }
        }

        String finalPath = placeholderHelper.replacePlaceholders(path, properties);

        String url = rpcClient.url();

        // 如果没有定义消费端数据格式,默认 application/json
        String contentType = "application/json";
        if (ArrayUtils.isNotEmpty(consumes)) {
            contentType = consumes[0];
        }

        log.info("请求方式[{}],基础路径[{}],相对路径[{}],主体格式[{}]", requestMethods[0], url, finalPath, contentType);

        switch (requestMethods[0]) {
            case POST:
                break;
            case GET:
                // 拼接 url 参数值
                MultiValueMap<String, String> params = new LinkedMultiValueMap();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    String parameterName = parameterNames[i];
                    RequestParam requestParam = AnnotationUtils.getAnnotation(parameterType, RequestParam.class);
                    if (requestParam != null) {
                        parameterName = requestParam.name();
                    }
                    if (ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType == String.class) {
                        params.add(parameterName, Objects.toString(args[i]));
                    } else {
                        // 如果参数是对象,解析每一个属性
                        PropertyDescriptor[] beanGetters = ReflectUtils.getBeanGetters(parameterType);
                        for (PropertyDescriptor beanGetter : beanGetters) {
                            String name = beanGetter.getName();
                            Method readMethod = beanGetter.getReadMethod();
                            Class<?> propertyType = beanGetter.getPropertyType();
                            if (ClassUtils.isPrimitiveOrWrapper(propertyType) || propertyType == String.class) {
                                Object invokeMethod = ReflectionUtils.invokeMethod(readMethod, args[i]);
                                params.add(name, Objects.toString(invokeMethod));
                            }
                        }
                    }
                }

//                    UriBuilder uriBuilder = new
//                    URI build = URIBuilder.fromUri(url).queryParams(parameters).build();
//                UriComponents build = UriComponentsBuilder.fromUriString(url).path(finalPath).queryParams(params).build();
//                log.info("完整请求 [{}]", build.toUriString());
//
//                responseEntity = restTemplate.exchange(build.encode().toUri(), HttpMethod.GET, null, returnType);

                log.info("发起请求 url[{}],path:[{}],params:[{}]",url,path,params);
                break;
        }


//        HttpStatus statusCode = responseEntity.getStatusCode();
//        if (statusCode == HttpStatus.OK) {
//            Object entityBody = responseEntity.getBody();
//
//            return entityBody;
//        }
//        throw new IllegalStateException(statusCode.getReasonPhrase());

        return "success";
    }

}

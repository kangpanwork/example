package com.sanri.test.testmvc.proxy;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

@Slf4j
public class Main {

    ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("{",  "}",":",true);

    public class MyInvocationHandler implements InvocationHandler{
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())) {
                log.info("当前是 Object 中的方法调用 "+method);
                return method.invoke(this,args);
            }
            Class<?> returnType = method.getReturnType();
            Class<?>[] parameterTypes = method.getParameterTypes();

            Class<?> declaringClass = method.getDeclaringClass();
            RpcClient rpcClient = AnnotationUtils.findAnnotation(declaringClass, RpcClient.class);

            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);
            RequestMethod[] requestMethods = requestMapping.method();
            String[] paths = requestMapping.value();
            String[] consumes = requestMapping.consumes();
            String[] headers = requestMapping.headers();

            MultiValueMap headersMap = convertHeaders(headers);

            RestTemplate restTemplate = new RestTemplate();
            String path = paths[0];
           Properties properties = new Properties();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                String parameterName = parameterNames[i];
                RequestParam requestParam = AnnotationUtils.getAnnotation(parameterType, RequestParam.class);
                if(requestParam != null){
                    parameterName = requestParam.name();
                }

                Object arg = args[i];
                if(ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType == String.class){
                    properties.put(parameterName, arg);
                }
            }

            String finalPath = placeholderHelper.replacePlaceholders(path, properties);
            ResponseEntity<?> responseEntity = null;

            String url = rpcClient.url();

            // 如果没有定义消费端数据格式,默认 application/json
            String contentType = MediaType.APPLICATION_JSON.toString();
            if(ArrayUtils.isNotEmpty(consumes)){
                contentType = consumes[0];
            }

            log.info("请求方式[{}],基础路径[{}],相对路径[{}],主体格式[{}]",requestMethods[0],url,finalPath,contentType);

            switch (requestMethods[0]){
                case POST:
                    MediaType mediaType = MediaType.parseMediaType(contentType);
                    if(MediaType.APPLICATION_JSON.isCompatibleWith( mediaType)){
                        headersMap.add("Content-Type",contentType);
                        // 如果使用 json 形式,应该方法是只有一个参数的
                        UriComponents build = UriComponentsBuilder.fromUriString(url).path(finalPath).build();
                        if(args != null && args.length > 0) {
                            String data = JSON.toJSONString(args[0]);
                            HttpEntity<String> httpEntity = new HttpEntity<String>(data, headersMap);
                            responseEntity =restTemplate.exchange(build.encode().toUri(), HttpMethod.POST, httpEntity, returnType);
                        }else {
                            responseEntity =restTemplate.exchange(build.encode().toUri(), HttpMethod.POST, null, returnType);
                        }
                    }else if(MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)){
                        // 拼接 url 参数值
                        MultiValueMap<String, String> params = convertParams(args, parameterTypes, parameterNames);
                        String data = convertMultiValueToKeyValue(params);
                        headersMap.add("Content-Type",contentType);
                        HttpEntity<String> httpEntity = new HttpEntity<String>(data, headersMap);
                        UriComponents build = UriComponentsBuilder.fromUriString(url).path(finalPath).build();
                        responseEntity =restTemplate.exchange(build.encode().toUri(), HttpMethod.POST, httpEntity, returnType);
                    }
                    break;
                case GET:
                    // 拼接 url 参数值
                    MultiValueMap<String, String> params = convertParams(args, parameterTypes, parameterNames);

                    UriComponents build = UriComponentsBuilder.fromUriString(url).path(finalPath).queryParams(params).build();
                    log.info("完整请求 [{}]",build.toUriString());

                    responseEntity = restTemplate.exchange(build.encode().toUri(), HttpMethod.GET, null, returnType);
                    break;
            }

            HttpStatus statusCode = responseEntity.getStatusCode();
            if(statusCode == HttpStatus.OK){
                Object entityBody = responseEntity.getBody();

                return entityBody;
            }
            throw new IllegalStateException(statusCode.getReasonPhrase());
        }

        private String convertMultiValueToKeyValue(MultiValueMap<String, String> params) {
//            if(params != null && !params.isEmpty()){
//                Iterator<Map.Entry<String, List<String>>> iterator = params.entrySet().iterator();
//                StringBuffer stringBuffer = new StringBuffer();
//                while (iterator.hasNext()){
//                    Map.Entry<String, List<String>> listEntry = iterator.next();
//                    String key = listEntry.getKey();
//                    List<String> values = listEntry.getValue();
//                    stringBuffer.append("&").append(key).append("=").append(org.apache.commons.lang3.StringUtils.join(values,','));
//                }
//                return stringBuffer.substring(1);
//            }
//            return "";
            UriComponents build = UriComponentsBuilder.fromPath("").queryParams(params).build();
            String keyValue = build.encode().toUriString();
            if(org.apache.commons.lang3.StringUtils.isNotBlank(keyValue))
                return keyValue.substring(1);

            return keyValue;
        }

        private MultiValueMap<String, String> convertParams(Object[] args, Class<?>[] parameterTypes, String[] parameterNames) {
            MultiValueMap<String,String> params = new LinkedMultiValueMap();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                String parameterName = parameterNames[i];
                RequestParam requestParam = AnnotationUtils.getAnnotation(parameterType, RequestParam.class);
                if(requestParam != null){
                    parameterName = requestParam.name();
                }
                if(ClassUtils.isPrimitiveOrWrapper(parameterType) || parameterType == String.class){
                    params.add(parameterName, Objects.toString(args[i]));
                }else{
                    // 如果参数是对象,解析每一个属性
                    PropertyDescriptor[] beanGetters = ReflectUtils.getBeanGetters(parameterType);
                    for (PropertyDescriptor beanGetter : beanGetters) {
                        String name = beanGetter.getName();
                        Method readMethod = beanGetter.getReadMethod();
                        Class<?> propertyType = beanGetter.getPropertyType();
                        if(ClassUtils.isPrimitiveOrWrapper(propertyType) || propertyType == String.class){
                            Object invokeMethod = ReflectionUtils.invokeMethod(readMethod, args[i]);
                            params.add(name,Objects.toString(invokeMethod));
                        }else if(propertyType == Date.class){
                            Object invokeMethod = ReflectionUtils.invokeMethod(readMethod, args[i]);
                            if(invokeMethod == null){
                                params.add(name,null);
                                continue;
                            }

                            DateTimeFormat dateTimeFormat = AnnotationUtils.getAnnotation(propertyType, DateTimeFormat.class);
                            if(dateTimeFormat != null){
                                String pattern = dateTimeFormat.pattern();
                                DateFormatUtils.format((Date) invokeMethod,pattern);
                            }else{
                                params.add(name,Objects.toString(invokeMethod));
                            }
                        }
                    }
                }
            }
            return params;
        }

        private MultiValueMap convertHeaders(String[] headers) {
            MultiValueMap<String,String> map = new LinkedMultiValueMap();
            if(ArrayUtils.isNotEmpty(headers)){
                for (String header : headers) {
                    String[] keyValues = StringUtils.split(header, "=");
                    if(keyValues.length < 2)continue;
                    String key = keyValues[0];
                    String values = keyValues[1];
                    String[] valuesArray = StringUtils.split(values, ",");
                    map.put(key,Arrays.asList(valuesArray));
                }
            }
            return map;
        }
    }

    @Test
    public void testProxy(){
        InterfaceTarget proxyInstance = (InterfaceTarget)Proxy.newProxyInstance(Main.class.getClassLoader(), new Class[]{InterfaceTarget.class}, new MyInvocationHandler());
//        System.out.println(proxyInstance);
//        String abc = proxyInstance.queryByName("world");
//        System.out.println(abc);

//        Emp sanri = new Emp("sanri", 10, new Date());
//        String postSanri = proxyInstance.postEmpNormal(sanri);
//        System.out.println(postSanri);

//        String postSanri = proxyInstance.postEmpJson(sanri);
//        System.out.println(postSanri);

//        Emp instanceEmp = proxyInstance.findEmp("abc");
//        System.out.println(instanceEmp);

        Emp emp = proxyInstance.emptyQuery();
        System.out.println(emp);
    }
}

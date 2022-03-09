package com.sanri.test.testmvc.dispatchbiz;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class AuditMappingHandlerMapping implements InitializingBean {
    private final MappingRegistry mappingRegistry = new MappingRegistry();
    @Autowired
    ListableBeanFactory beanFactory;

    class MappingRegistry {
        private final Map<String, HandlerMethod> urlLookup = new HashMap<>();

        public void register(String url,Object handler, Method method){
            HandlerMethod handlerMethod = new HandlerMethod(handler, method);
            urlLookup.put(url,handlerMethod);
        }

        public HandlerMethod lookupHandlerMethod(String lookupPath){
            return urlLookup.get(lookupPath);
        }

        public void register(String pattern, HandlerMethod handlerMethod) {
            urlLookup.put(pattern,handlerMethod);
        }
    }

    public void handle(AuditEventExtend auditEventExtend) throws InvocationTargetException, IllegalAccessException {
        String buildUrl = auditEventExtend.buildUrl();
        HandlerMethod handlerMethod = mappingRegistry.lookupHandlerMethod(buildUrl);
        Method method = handlerMethod.getMethod();
        Object bean = handlerMethod.getBean();
        if(bean instanceof String){
            bean = beanFactory.getBean(Objects.toString(bean));
        }
        method.invoke(bean,auditEventExtend);
    }

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> iterator = handlerMethods.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<RequestMappingInfo, HandlerMethod> mappingInfoHandlerMethodEntry = iterator.next();
            RequestMappingInfo requestMappingInfo = mappingInfoHandlerMethodEntry.getKey();
            HandlerMethod handlerMethod = mappingInfoHandlerMethodEntry.getValue();
            Class<?> beanType = handlerMethod.getBeanType();
            if(Audit.class.isAssignableFrom(beanType)) {
                Set<String> patterns = requestMappingInfo.getPatternsCondition().getPatterns();
                for (String pattern : patterns) {
                    mappingRegistry.register(pattern, handlerMethod);
                }
            }
        }
    }
}

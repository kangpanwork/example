package com.sanri.test.testmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StrategyHelloBizMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private Map<String,HelloBiz> helloBizMap = new HashMap<>();

    public StrategyHelloBizMethodArgumentResolver(Map<String, HelloBiz> helloBizMap) {
        this.helloBizMap = helloBizMap;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(HelloBiz.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String company = webRequest.getParameter("company");
        for (HelloBiz value : helloBizMap.values()) {
            if (value instanceof HelloBizFallback){
                continue;
            }
            if (value.support(company)){
                return value;
            }
        }
        return helloBizMap.get("helloBizFallBack");
    }
}

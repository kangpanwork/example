package com.sanri.test.testmvc.config;

import com.sanri.test.testmvc.dto.SystemUser;
import com.sanri.test.testmvc.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ConfigMethodArgumentResolver implements HandlerMethodArgumentResolver {
    UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SystemUser.class);
    }

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if(StringUtils.isBlank(authorization)){
            return null;
        }
        Claims body = Jwts.parser().setSigningKey(userService.signKey(signatureAlgorithm)).parseClaimsJws(authorization).getBody();
        String userId = body.get("userId", String.class);
        String username = body.get("username", String.class);

        return new SystemUser(userId,username);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

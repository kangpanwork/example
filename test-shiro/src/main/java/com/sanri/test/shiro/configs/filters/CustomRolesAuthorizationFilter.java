package com.sanri.test.shiro.configs.filters;

import com.alibaba.fastjson.JSONObject;
import com.sanri.test.shiro.configs.exception.SystemMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class CustomRolesAuthorizationFilter extends RolesAuthorizationFilter {
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(SystemMessage.PERMISSION_DENIED.result()));
    }
}

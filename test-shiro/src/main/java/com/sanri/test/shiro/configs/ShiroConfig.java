package com.sanri.test.shiro.configs;

import com.sanri.test.shiro.configs.filters.CustomFormAuthenticationFilter;
import com.sanri.test.shiro.configs.filters.CustomPermissionsAuthorizationFilter;
import com.sanri.test.shiro.configs.filters.CustomRolesAuthorizationFilter;
import com.sanri.test.shiro.mapper.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    public SecurityManager securityManager(Realm realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = staticUrls();
        filterChainDefinitionMap.putAll(anonUrls());
        filterChainDefinitionMap.putAll(permUrls());

        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authc", new CustomFormAuthenticationFilter());
        filters.put("perms", new CustomPermissionsAuthorizationFilter());
        filters.put("roles", new CustomRolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filters);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 不拦截的静态资源路径
     * @return
     */
    Map<String,String> staticUrls(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs/**", "anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/static/**","anon");
        filterChainDefinitionMap.put("/index.html","anon");
        return filterChainDefinitionMap;
    }

    /**
     * 不拦截的业务路径
     * @return
     */
    Map<String,String> anonUrls(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/","anon");
        filterChainDefinitionMap.put("/v1/login","anon");
        filterChainDefinitionMap.put("/v1/logout","anon");
        return filterChainDefinitionMap;
    }

    /**
     *
     * @return
     */
    Map<String,String> permUrls(){
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/v1/admin/**","authc,roles[admin]");
        filterChainDefinitionMap.put("/v1/normal/**","authc");
        filterChainDefinitionMap.put("/**","authc");
        return filterChainDefinitionMap;
    }
}

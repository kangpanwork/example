package com.sanri.test.testwebui.service;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Component
public class XXServiceAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Autowired
    private XXServiceInterceptor xxServiceInterceptor;

    @Override
    public Pointcut getPointcut() {
//        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
//        aspectJExpressionPointcut.setExpression("execution(* com.sanri.test.testwebui.controller.StrategyController.*(..))");
//        return aspectJExpressionPointcut;
       return new ControllerMatchPointcut();
    }

    class ControllerMatchPointcut extends StaticMethodMatcherPointcut{

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(targetClass);
            if(ArrayUtils.contains(allInterfacesForClass,XXServiceAware.class))return true;

            return false;
        }
    }

    @Override
    public Advice getAdvice() {
        return xxServiceInterceptor;
    }
}

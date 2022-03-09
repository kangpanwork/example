package com.sanri.test.testwebui.configs;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 自己定义一个切面，实现一个 advisor 注入到 spring 容器就好了
 */
@Component
public class MyAopAdvisor implements PointcutAdvisor {

    @Override
    public Pointcut getPointcut() {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression("execution(* com.sanri.test.testwebui.controller.TestController.*(..))");
        return aspectJExpressionPointcut;
    }

    @Override
    public Advice getAdvice() {
        return new MethodInterceptor(){

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Method method = invocation.getMethod();
                System.out.println("方法被调用:"+method.getName());
                return invocation.proceed();
            }
        };
    }

    @Override
    public boolean isPerInstance() {
        return true;
    }
}

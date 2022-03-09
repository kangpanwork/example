package com.sanri.test.testmybatis.configs;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts(
        // 拦截 Executor.query 方法，不是查缓存那个
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})
)
@Slf4j
public class TimeSpendInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StopWatch stopWatch = new StopWatch();stopWatch.start();
        // Invocation 的参数是看拦截的方法来的，这里 query 方法第一个参数是语句对象
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String id = mappedStatement.getId();
        Object proceed = null;
        try {
            proceed =  invocation.proceed();
        }finally {
            stopWatch.stop();
            log.info("sqlId:"+id+" 执行时间为:"+stopWatch.getTime()+" ms");
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }
}

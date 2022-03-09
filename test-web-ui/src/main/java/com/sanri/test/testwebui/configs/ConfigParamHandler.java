package com.sanri.test.testwebui.configs;

import com.sanri.web.configs.ParamHandler;
import com.sanri.web.configs.ResponseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigParamHandler {

    static class CustomParamHandler implements ParamHandler{
        @Override
        public String handler(String origin) {
            return origin.replace("<" ,"《");
        }
    }

    @Bean("paramHandler")
    public ParamHandler paramHandler(){
        return new CustomParamHandler();
    }

//    @Bean
//    public ResponseHandler responseHandler(){
//        return new CustomResponseHandler();
//    }

//    @Bean("customLogInfoHandler")
//    public LogInfoHandler logInfoHandler(){
//        return new LogInfoHandler() {
//            @Override
//            public void before(LogInfo logInfo) {
//                System.out.println("自定义日志输出:"+logInfo);
//            }
//
//            @Override
//            public void around(AroundLogInfo aroundLogInfo) {
//
//            }
//        };
//    }
}

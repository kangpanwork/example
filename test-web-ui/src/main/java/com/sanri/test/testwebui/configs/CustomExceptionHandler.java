package com.sanri.test.testwebui.configs;

import com.sanri.web.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 用户自定义拦截，将有最高优先级
 */
@RestControllerAdvice
@Slf4j
//@Order(Ordered.LOWEST_PRECEDENCE - 20)
public class CustomExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDto exception(IllegalArgumentException e){

        return ResponseDto.err("222").message(e.getMessage());
    }

//    @ExceptionHandler
//    public void aaa(){
//
//    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseDto bbb(IllegalStateException e){
        return ResponseDto.err("33").message(e.getMessage());
    }

//    @ExceptionHandler({IllegalStateException.class})
//    public void bbb(IllegalStateException e){
//        System.out.println(e);
//    }
}

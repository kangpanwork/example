package com.sanri.test.testwebui.configs;


import com.sanri.web.configs.ResponseHandler;
import com.sanri.web.dto.ResponseDto;

public class CustomResponseHandler implements ResponseHandler {
    @Override
    public Object handlerOut(ResponseDto responseDto) {
        return new ResultDto(responseDto.getCode(),responseDto.getMessage(),responseDto.getData());
    }

    @Override
    public Object handlerError(ResponseDto responseDto) {
        return responseDto.getCode()+":"+responseDto.getMessage();
    }
}

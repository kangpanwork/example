package com.sanri.test.shiro.configs.exception;

import com.sanri.test.shiro.configs.ResponseDto;

import java.text.MessageFormat;

public interface ExceptionCause<T extends Exception> {
    T exception(Object... args);

    ResponseDto result();

    MessageFormat getMessageFormat();
}

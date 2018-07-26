package org.delphy.tokenheroserver.handler;

import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.common.RestResult;
import org.delphy.tokenheroserver.exception.DefaultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

/**
 * @author mutouji
 */
@ControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(value = DefaultException.class)
    public ResponseEntity<RestResult> handleServiceException(DefaultException exception) {
        EnumError errorCode = exception.getErrorCode();
        return new ResponseEntity<>(new RestResult<>(errorCode.getCode(), errorCode.getMsg(), ""), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RestResult> defaultErrorHandler(Exception e) {
        e.printStackTrace();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String msg = e.getMessage();
        Class exceptionClazz = e.getClass();
        if (Objects.equals(MissingServletRequestParameterException.class, exceptionClazz)) {
            msg = "incorrect parameter";
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (Objects.equals(HttpRequestMethodNotSupportedException.class, exceptionClazz)) {
            httpStatus = HttpStatus.BAD_REQUEST;
            msg = e.getMessage();
        }
        return new ResponseEntity<>(new RestResult<>(httpStatus.value(), msg, ""), httpStatus);
    }
}

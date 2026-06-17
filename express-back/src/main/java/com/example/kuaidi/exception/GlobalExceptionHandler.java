package com.example.kuaidi.exception;

import com.example.kuaidi.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<?> handleBusinessException(BusinessException e) {
        return ResultVO.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<?> handleException(Exception e) {
        e.printStackTrace();
        return ResultVO.error("系统内部错误");
    }
}

package com.gker.gkerlove.exception;

import com.gker.gkerlove.bean.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R handleGlobalError(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error().message("服务器出错，请重试");
    }

    @ExceptionHandler(BadRequest.class)
    public R handleBadRequest() {
        return R.error().message("请求非法!");
    }

    @ExceptionHandler(GKerLoveException.class)
    public R handleTrainsException(GKerLoveException e) {
        return R.error().message(e.getMessage());
    }
}
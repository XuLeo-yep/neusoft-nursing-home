package org.example.demo.handler;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotPermissionException;
import org.example.demo.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p></p>
 *
 * @author li253
 * @version 1.0
 * @date 2026/5/24 15:12
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 认证异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result notPermissionException(NotPermissionException e){
        SaHolder.getResponse().setStatus(403);
        return Result.error(e.getMessage());
    }
}

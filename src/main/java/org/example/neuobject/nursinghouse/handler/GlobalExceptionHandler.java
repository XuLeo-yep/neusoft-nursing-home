package org.example.neuobject.nursinghouse.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import org.example.neuobject.nursinghouse.common.Result;
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
    // 登录异常 → 401，前端自动跳登录页
    @ExceptionHandler(NotLoginException.class)
    public Result notLoginException(NotLoginException e) {
        SaHolder.getResponse().setStatus(401);
        return Result.error(401, e.getMessage());
    }
    // 通用异常 → 500，兜底
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return Result.error(500, e.getMessage());
    }
}

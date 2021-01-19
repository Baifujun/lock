package com.chh.lock.handler;

import com.alibaba.fastjson.JSONObject;
import com.chh.lock.vo.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SysExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public JSONObject exception(Exception exception) {
        log.error("系统出现异常，异常信息：{}", exception.getMessage());
        return ResultTool.ResultMap(2, null, "门锁系统出错了，请联系门锁服务商！原因：" + exception.getMessage());
    }
}

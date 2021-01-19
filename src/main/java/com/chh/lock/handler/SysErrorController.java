package com.chh.lock.handler;

import com.alibaba.fastjson.JSONObject;
import com.chh.lock.vo.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class SysErrorController extends AbstractErrorController {

    public SysErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping
    public JSONObject error(HttpServletRequest request) {
        log.error("未找到处理器，URI：{}" + request.getRequestURI());
        return ResultTool.ResultMap(3, null, "未找到处理器，URI：" + request.getRequestURI());
    }

}

package com.chh.lock.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultTool {
    public static JSONObject ResultMap(int code, Object data, Object msg) {
        JSONObject result = new JSONObject();
        result.put("code", code);
        result.put("data", data);
        result.put("msg", msg);
        log.info(result.toString());
        return result;
    }
}

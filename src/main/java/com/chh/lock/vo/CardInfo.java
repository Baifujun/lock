package com.chh.lock.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardInfo {
    // 房间号
    private String roomNo;
    // 离店时间
    private String checkOutTime;
    // 客人姓名
    private String userName;
    // OTA订单号
    private String orderCode;
    // 入住组单号
    private String checkInCode;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

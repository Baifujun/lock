package com.chh.lock.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockResponse {
    // 消息头
    // 包含消息头及载荷的总长度
    private long totalLen;
    // 消息类型，客户端固定0x05，服务端固定0x8005
    private long msgType;
    // 消息序号，进行消息计数（请求响应保持一致）
    private long seq;
    // 固定值1
    private long contentCount;

    // 载荷
    // 0代表成功，其他代表失败
    private int code;
    // 返回码说明信息
    private String msg;
    // 卡箱/回收箱状态，0：卡箱正常 1：卡箱预空 2：卡箱预满 3：卡箱空 4卡箱满 5：回收箱正常 6:回收箱满
    private int info;
    // 卡箱/回收箱状态码说明信息
    private String infoMsg;
    // 房间号：楼栋+楼层+房间号+子房间号
    private String roomNO;
    // 入住时间
    private String checkinTime;
    // 离店时间
    private String checkoutTime;
    // 授权信息，当授权最后一个月时，该字段会提示说明，提醒用户重新授权
    private String authorizationInfo;
}

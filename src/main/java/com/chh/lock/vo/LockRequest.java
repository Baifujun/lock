package com.chh.lock.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockRequest {
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
    // 1代表发卡，0代表收卡，2代表查询卡片 3代表查询卡箱状态，4代表查询回收箱状态
    private byte isCheckin;
    // 房间号：楼栋+楼层+房号+子房号(各两字节，必达使用前六个字节，其他补零)
    private long roomNO;
    // 入住时间
    private byte[] checkinTime;
    // 离店时间
    private byte[] checkoutTime;
    // 用户姓名
    private byte[] username;
    // 是否能开反锁标志
    private byte lock;
    // 是否能开公共门标志
    private byte doors;
    // 早餐次数
    private byte breakfast;
    // 是否为复制卡0：非复制卡 1：复制卡
    private byte isCopy;
    // 套内门选号16种，当pdoors为1时有效。举例：宾客卡可以开启 01、02、07、08号套房内门，则置位二进制字符串为：“0000 0000 1100 0011”，转化为十六 进制字符串为：“00C3”。
    private int suitDoor;
    // 公共门选号32种，当pdoors为1时有效。举例：宾客卡可以开启 01、08、15号公共门，则置位二进制字符串为：“0000 0000 0000 0000 0100 0000 1000 0001”，转化为十六进制字符串为：“00004081”。
    private long pubDoor;
}

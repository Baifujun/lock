package com.chh.lock.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadCardInfo {
    // 房间号
    private String roomNo;
    // 离店时间
    private String endTime;
    // 开始时间
    private String startTime;

    @Override
    public String toString() {
        return "ReadCardInfo{" +
                "roomNo='" + roomNo + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}

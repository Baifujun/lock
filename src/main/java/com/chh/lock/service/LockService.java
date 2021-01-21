package com.chh.lock.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chh.lock.config.LockServerConfig;
import com.chh.lock.vo.LockResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Service
public class LockService {
    private final int headLength = 16;
    private final int msgType = 0x05;
    private final int contentCount = 1;
    private final int timeout = 30 * 1000;
    private final Random random = new Random();

    @Autowired
    private LockServerConfig lockServerConfig;

    /**
     * 读卡
     */
    public synchronized LockResponse read() throws Exception {
        log.info("==========开始执行读卡请求==========");
        LockResponse lockResponse = establishTCPConnection(0, "", "", "", "", 0, 0, 0, 0, "", "");
        log.info("==========执行读卡请求结束==========");
        return lockResponse;
    }

    /**
     * 制卡
     */
    public synchronized LockResponse write(String roomNo
            , String checkinTime
            , String checkoutTime
            , String username
            , int lock
            , int doors
            , int breakfast
            , int isCopy
            , String suitDoor
            , String pubDoor) throws Exception {
        log.info("==========开始执行制卡请求==========");
        LockResponse lockResponse = establishTCPConnection(1, roomNo, checkinTime, checkoutTime, username, lock, doors, breakfast, isCopy, suitDoor, pubDoor);
        log.info("==========执行制卡请求结束==========");
        return lockResponse;
    }

    /**
     * 销卡
     */
    public synchronized LockResponse erase() throws Exception {
        log.info("==========开始执行销卡请求==========");
        LockResponse lockResponse = establishTCPConnection(2, "", "", "", "", 0, 0, 0, 0, "", "");
        log.info("==========执行销卡请求结束==========");
        return lockResponse;
    }

    /**
     * 卡箱状态
     */
    public synchronized LockResponse cardBox() throws Exception {
        log.info("==========开始执行查询卡箱请求==========");
        LockResponse lockResponse = establishTCPConnection(3, "", "", "", "", 0, 0, 0, 0, "", "");
        log.info("==========执行查询卡箱请求结束==========");
        return lockResponse;
    }

    /**
     * 回收箱状态
     */
    public synchronized LockResponse recyclingBox() throws Exception {
        log.info("==========开始执行查询回收箱请求==========");
        LockResponse lockResponse = establishTCPConnection(4, "", "", "", "", 0, 0, 0, 0, "", "");
        log.info("==========执行查询回收箱请求结束==========");
        return lockResponse;
    }

    /**
     * 续住读卡
     */
    public synchronized LockResponse continueRead() throws Exception {
        log.info("==========开始执行续住读卡请求==========");
        LockResponse lockResponse = establishTCPConnection(5, "", "", "", "", 0, 0, 0, 0, "", "");
        log.info("==========执行续住读卡请求结束==========");
        return lockResponse;
    }

    /**
     * 续住制卡
     */
    public synchronized LockResponse continueWrite(String roomNo
            , String checkinTime
            , String checkoutTime
            , String username
            , int lock
            , int doors
            , int breakfast
            , int isCopy
            , String suitDoor
            , String pubDoor) throws Exception {
        log.info("==========开始执行续住制卡请求==========");
        LockResponse lockResponse = establishTCPConnection(6, roomNo, checkinTime, checkoutTime, username, lock, doors, breakfast, isCopy, suitDoor, pubDoor);
        log.info("==========执行续住制卡请求结束==========");
        return lockResponse;
    }

    /**
     * 建立TCP连接
     */
    private LockResponse establishTCPConnection(int isCheckin, String roomNo, String checkinTime, String checkoutTime, String username, int lock, int doors, int breakfast, int isCopy, String suitDoor, String pubDoor) throws Exception {
        Socket socket = null;
        try {
            // 请求体
            JSONObject lockRequestBodyObject = new JSONObject();
            lockRequestBodyObject.put("Ischeckin", isCheckin);
            lockRequestBodyObject.put("RoomNO", roomNo);
            lockRequestBodyObject.put("Checkintime", checkinTime);
            lockRequestBodyObject.put("Checkouttime", checkoutTime);
            lockRequestBodyObject.put("Username", username);
            lockRequestBodyObject.put("LLock", lock);
            lockRequestBodyObject.put("pdoors", doors);
            lockRequestBodyObject.put("Breakfast", breakfast);
            lockRequestBodyObject.put("Iscopy", isCopy);
            lockRequestBodyObject.put("SuitDoor", suitDoor);
            lockRequestBodyObject.put("PubDoor", pubDoor);
            final byte[] lockRequestBodyByteArray = lockRequestBodyObject.toString().getBytes("GB2312");

            // 请求头
            final int seq = Math.abs(random.nextInt());
            ByteBuffer lockRequestHeadBuffer = ByteBuffer.allocate(headLength);
            lockRequestHeadBuffer.putInt(headLength + lockRequestBodyByteArray.length);
            lockRequestHeadBuffer.putInt(msgType);
            lockRequestHeadBuffer.putInt(seq);
            lockRequestHeadBuffer.putInt(contentCount);

            // 请求报文
            ByteBuffer lockRequestBuffer = ByteBuffer.allocate(headLength + lockRequestBodyByteArray.length);
            lockRequestBuffer.put(lockRequestHeadBuffer.array());
            lockRequestBuffer.put(lockRequestBodyByteArray);

            // 请求转换为自己的格式
            JSONObject lockRequestObject = new JSONObject();
            lockRequestObject.put("totalLen", headLength + lockRequestBodyByteArray.length);
            lockRequestObject.put("msgType", msgType);
            lockRequestObject.put("seq", seq);
            lockRequestObject.put("contentCount", contentCount);
            lockRequestObject.put("isCheckin", isCheckin);
            lockRequestObject.put("roomNO", roomNo);
            lockRequestObject.put("checkinTime", checkinTime);
            lockRequestObject.put("checkoutTime", checkoutTime);
            lockRequestObject.put("username", username);
            lockRequestObject.put("lock", lock);
            lockRequestObject.put("doors", doors);
            lockRequestObject.put("breakfast", breakfast);
            lockRequestObject.put("isCopy", isCopy);
            lockRequestObject.put("suitDoor", suitDoor);
            lockRequestObject.put("pubDoor", pubDoor);
            log.info("请求报文：\n" + lockRequestObject.toString());

            log.info("请求byte序列：\n" + Hex.encodeHexString(lockRequestBuffer.array()));

            // 建立TCP连接，并获取响应
            socket = new Socket(lockServerConfig.getIp(), lockServerConfig.getPort());
            socket.setSoTimeout(timeout);
            OutputStream outputStream = socket.getOutputStream();
            byte[] request = lockRequestBuffer.array();
            outputStream.write(request);
            outputStream.flush();
            InputStream inputStream = socket.getInputStream();
            byte[] tempBuffer = new byte[102400];
            int length = inputStream.read(tempBuffer);
            byte[] responseBuffer = Arrays.copyOf(tempBuffer, length);
            //IOUtils.read(inputStream, responseBuffer);
            log.info("响应byte序列：\n" + Hex.encodeHexString(responseBuffer));
            ByteBuf byteBuf = Unpooled.wrappedBuffer(responseBuffer);

            // 处理响应头
            byte[] responseHead = new byte[headLength];
            byteBuf.getBytes(0, responseHead, 0, headLength);
            ByteBuffer responseHeadBuffer = ByteBuffer.allocate(headLength);
            responseHeadBuffer.put(responseHead);

            // 处理响应体
            byte[] responseBody = new byte[responseBuffer.length - headLength];
            byteBuf.getBytes(headLength, responseBody, 0, responseBuffer.length - headLength);
            final JSONObject responseObject = JSON.parseObject(new String(responseBody, "GB2312"));

            final LockResponse lockResponse = LockResponse.builder()
                    .totalLen(responseHeadBuffer.getInt(0))
                    .msgType(responseHeadBuffer.getInt(4))
                    .seq(responseHeadBuffer.getInt(8))
                    .contentCount(responseHeadBuffer.getInt(12))
                    .code(responseObject.getInteger("Code"))
                    .msg(responseObject.getString("Msg"))
                    .info(responseObject.getInteger("Info"))
                    .infoMsg(responseObject.getString("Info_Msg"))
                    .roomNO(responseObject.getString("RoomNO"))
                    .checkinTime(responseObject.getString("Checkintime"))
                    .checkoutTime(responseObject.getString("Checkouttime"))
                    .authorizationInfo(responseObject.getString("AuthorizationInfo"))
                    .build();
            log.info("响应报文：\n" + JSON.toJSON(lockResponse));

            return lockResponse;
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}

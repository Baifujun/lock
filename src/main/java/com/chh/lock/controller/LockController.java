package com.chh.lock.controller;

import com.alibaba.fastjson.JSONObject;
import com.chh.lock.service.LockService;
import com.chh.lock.vo.CardInfo;
import com.chh.lock.vo.LockResponse;
import com.chh.lock.vo.ReadCardInfo;
import com.chh.lock.vo.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/lock")
public class LockController {

    @Autowired
    LockService lockService;

    @GetMapping("/read")
    public LockResponse read() throws IOException {
        return lockService.read();
    }

    @GetMapping("/write")
    public LockResponse write(String roomNo
            , String checkinTime
            , String checkoutTime
            , String username
            , int lock
            , int doors
            , int breakfast
            , int isCopy
            , String suitDoor
            , String pubDoor) throws IOException {
        return lockService.write(roomNo, checkinTime, checkoutTime, username, lock, doors, breakfast, isCopy, suitDoor, pubDoor);
    }

    @GetMapping("/erase")
    public LockResponse erase() throws IOException {
        return lockService.erase();
    }

    @GetMapping("/continueread")
    public LockResponse continueRead() throws IOException {
        return lockService.continueRead();
    }

    @GetMapping("/continuewrite")
    public LockResponse continueWrite(String roomNo
            , String checkinTime
            , String checkoutTime
            , String username
            , int lock
            , int doors
            , int breakfast
            , int isCopy
            , String suitDoor
            , String pubDoor) throws IOException {
        return lockService.continueWrite(roomNo, checkinTime, checkoutTime, username, lock, doors, breakfast, isCopy, suitDoor, pubDoor);
    }

    @GetMapping("/cardbox")
    public LockResponse cardBox() throws IOException {
        return lockService.cardBox();
    }

    @GetMapping("/recyclingbox")
    public LockResponse recyclingBox() throws IOException {
        return lockService.recyclingBox();
    }

    @GetMapping("/read/v1")
    public JSONObject readV1() throws IOException {
        LockResponse lockResponse = lockService.read();
        if (lockResponse.getCode() == 0) {
            ReadCardInfo cardInfo = new ReadCardInfo(lockResponse.getRoomNO(), lockResponse.getCheckoutTime(), lockResponse.getCheckinTime());
            return ResultTool.ResultMap(0, cardInfo, "读卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), null, "读卡失败！" + lockResponse.getMsg());
    }

    @GetMapping("/write/v1")
    public JSONObject writeV1(CardInfo cardInfo) throws IOException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyMMddHHmm");
        LockResponse lockResponse = lockService.write(cardInfo.getRoomNo(), pattern.format(LocalDateTime.now()), cardInfo.getCheckOutTime(), cardInfo.getUserName(), 1, 1, 1, 0, "01", "01");
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, cardInfo, "制卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), null, "制卡失败！" + lockResponse.getMsg());

    }

    @GetMapping("/erase/v1")
    public JSONObject eraseV1() throws IOException {
        LockResponse lockResponse = lockService.erase();
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, null, "销卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), null, "销卡失败！" + lockResponse.getMsg());
    }

    @GetMapping("/cardbox/v1")
    public JSONObject cardBoxV1() throws IOException {
        LockResponse lockResponse = lockService.cardBox();
        if (lockResponse.getInfo() == 0) {
            return ResultTool.ResultMap(0, "卡箱有卡", "卡箱有卡");
        } else if (lockResponse.getInfo() == 1) {
            return ResultTool.ResultMap(0, "卡箱预空", "卡箱预空");
        } else if (lockResponse.getInfo() == 2) {
            return ResultTool.ResultMap(0, "卡箱预满", "卡箱预满");
        } else if (lockResponse.getInfo() == 3) {
            return ResultTool.ResultMap(1, "卡箱无卡", "卡箱无卡");
        } else if (lockResponse.getInfo() == 4) {
            return ResultTool.ResultMap(0, "卡箱已满", "卡箱已满");
        } else if (lockResponse.getInfo() == 5) {
            return ResultTool.ResultMap(0, "回收箱正常", "回收箱正常");
        } else if (lockResponse.getInfo() == 6) {
            return ResultTool.ResultMap(1, "回收箱满", "回收箱满");
        } else {
            return ResultTool.ResultMap(1, "门锁系统错误", "门锁系统错误");
        }
    }

    @GetMapping("/recyclingbox/v1")
    public LockResponse recyclingBoxV1() throws IOException {
        return lockService.recyclingBox();
    }

    @GetMapping("/continueread/v1")
    public JSONObject continueReadV1() throws IOException {
        LockResponse lockResponse = lockService.continueRead();
        if (lockResponse.getCode() == 0) {
            ReadCardInfo cardInfo = new ReadCardInfo(lockResponse.getRoomNO(), lockResponse.getCheckoutTime(), lockResponse.getCheckinTime());
            return ResultTool.ResultMap(0, cardInfo, "读卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), null, "读卡失败！" + lockResponse.getMsg());
    }

    @GetMapping("/continuewrite/v1")
    public JSONObject continueWriteV1(CardInfo cardInfo) throws IOException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyMMddHHmm");
        LockResponse lockResponse = lockService.continueWrite(cardInfo.getRoomNo(), pattern.format(LocalDateTime.now()), cardInfo.getCheckOutTime(), cardInfo.getUserName(), 1, 1, 1, 0, "01", "01");
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, cardInfo, "制卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), null, "制卡失败！" + lockResponse.getMsg());
    }

}

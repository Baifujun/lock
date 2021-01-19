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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/lock")
public class LockController {

    @Autowired
    LockService lockService;

    /**
     * 读卡
     */
    @GetMapping("/read")
    public LockResponse read() throws Exception {
        return lockService.read();
    }

    /**
     * 制卡
     */
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
            , String pubDoor) throws Exception {
        return lockService.write(roomNo, checkinTime, checkoutTime, username, lock, doors, breakfast, isCopy, suitDoor, pubDoor);
    }

    /**
     * 销卡
     */
    @GetMapping("/erase")
    public LockResponse erase() throws Exception {
        return lockService.erase();
    }

    /**
     * 续住读卡
     */
    @GetMapping("/continueread")
    public LockResponse continueRead() throws Exception {
        return lockService.continueRead();
    }

    /**
     * 续住制卡
     */
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
            , String pubDoor) throws Exception {
        return lockService.continueWrite(roomNo, checkinTime, checkoutTime, username, lock, doors, breakfast, isCopy, suitDoor, pubDoor);
    }

    /**
     * 卡箱状态
     */
    @GetMapping("/cardbox")
    public LockResponse cardBox() throws Exception {
        return lockService.cardBox();
    }

    /**
     * 回收箱状态
     */
    @GetMapping("/recyclingbox")
    public LockResponse recyclingBox() throws Exception {
        return lockService.recyclingBox();
    }

    /**
     * 读卡（FOR 主程序）
     */
    @GetMapping("/read/v1")
    public JSONObject readV1() throws Exception {
        LockResponse lockResponse = lockService.read();
        if (lockResponse.getCode() == 0) {
            ReadCardInfo cardInfo = new ReadCardInfo(lockResponse.getRoomNO(), lockResponse.getCheckoutTime(), lockResponse.getCheckinTime());
            return ResultTool.ResultMap(0, cardInfo, "读卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "读卡失败！" + lockResponse.getMsg());
    }

    /**
     * 制卡（FOR 主程序）
     */
    @GetMapping("/write/v1")
    public JSONObject writeV1(CardInfo cardInfo) throws Exception {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyMMddHHmm");
        LockResponse lockResponse = lockService.write(cardInfo.getRoomNo(), pattern.format(LocalDateTime.now()), cardInfo.getCheckOutTime(), cardInfo.getUserName(), 1, 1, 1, 0, "01", "01");
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, cardInfo, "制卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "制卡失败！" + lockResponse.getMsg());

    }

    /**
     * 销卡（FOR 主程序）
     */
    @GetMapping("/erase/v1")
    public JSONObject eraseV1() throws Exception {
        LockResponse lockResponse = lockService.erase();
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, null, "销卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "销卡失败！" + lockResponse.getMsg());
    }

    /**
     * 续住读卡（FOR 主程序）
     */
    @GetMapping("/continueread/v1")
    public JSONObject continueReadV1() throws Exception {
        LockResponse lockResponse = lockService.continueRead();
        if (lockResponse.getCode() == 0) {
            ReadCardInfo cardInfo = new ReadCardInfo(lockResponse.getRoomNO(), lockResponse.getCheckoutTime(), lockResponse.getCheckinTime());
            return ResultTool.ResultMap(0, cardInfo, "读卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "读卡失败！" + lockResponse.getMsg());
    }

    /**
     * 续住制卡（FOR 主程序）
     */
    @GetMapping("/continuewrite/v1")
    public JSONObject continueWriteV1(CardInfo cardInfo) throws Exception {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyMMddHHmm");
        LockResponse lockResponse = lockService.continueWrite(cardInfo.getRoomNo(), pattern.format(LocalDateTime.now()), cardInfo.getCheckOutTime(), cardInfo.getUserName(), 1, 1, 1, 0, "01", "01");
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, cardInfo, "制卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "制卡失败！" + lockResponse.getMsg());
    }


    /**
     * 卡箱状态（FOR 主程序）
     */
    @GetMapping("/cardbox/v1")
    public JSONObject cardBoxV1() throws Exception {
        LockResponse lockResponse = lockService.cardBox();
        if (lockResponse.getInfo() == 0) {
            return ResultTool.ResultMap(0, "卡箱有卡", "卡箱有卡");
        } else if (lockResponse.getInfo() == 1) {
            return ResultTool.ResultMap(0, "卡箱预空", "卡箱预空");
        } else if (lockResponse.getInfo() == 2) {
            return ResultTool.ResultMap(0, "卡箱预满", "卡箱预满");
        } else if (lockResponse.getInfo() == 3) {
            return ResultTool.ResultMap(1, lockResponse.getMsg(), "卡箱无卡");
        } else if (lockResponse.getInfo() == 4) {
            return ResultTool.ResultMap(0, "卡箱已满", "卡箱已满");
        } else {
            return ResultTool.ResultMap(1, lockResponse.getMsg(), "门锁系统错误");
        }
    }

    /**
     * 回收箱状态（FOR 主程序）
     */
    @GetMapping("/recyclingbox/v1")
    public JSONObject recyclingBoxV1() throws Exception {
        LockResponse lockResponse = lockService.recyclingBox();
        if (lockResponse.getInfo() == 5) {
            return ResultTool.ResultMap(0, "回收箱正常", "回收箱正常");
        } else if (lockResponse.getInfo() == 6) {
            return ResultTool.ResultMap(1, lockResponse.getMsg(), "回收箱满");
        } else {
            return ResultTool.ResultMap(1, lockResponse.getMsg(), "门锁系统错误");
        }
    }

}

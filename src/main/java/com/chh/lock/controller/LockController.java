package com.chh.lock.controller;

import com.alibaba.fastjson.JSONObject;
import com.chh.lock.service.LockService;
import com.chh.lock.vo.CardInfo;
import com.chh.lock.vo.LockResponse;
import com.chh.lock.vo.ReadCardInfo;
import com.chh.lock.vo.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/lock")
public class LockController {

    private final DateTimeFormatter LOCK_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmm");
    private final DateTimeFormatter TRANSPORT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    LockService lockService;

    /**
     * 读卡
     */
    @RequestMapping("/read")
    public LockResponse read() throws Exception {
        return lockService.read();
    }

    /**
     * 制卡
     */
    @RequestMapping("/write")
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
    @RequestMapping("/erase")
    public LockResponse erase() throws Exception {
        return lockService.erase();
    }

    /**
     * 续住读卡
     */
    @RequestMapping("/continueread")
    public LockResponse continueRead() throws Exception {
        return lockService.continueRead();
    }

    /**
     * 续住制卡
     */
    @RequestMapping("/continuewrite")
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
    @RequestMapping("/cardbox")
    public LockResponse cardBox() throws Exception {
        return lockService.cardBox();
    }

    /**
     * 回收箱状态
     */
    @RequestMapping("/recyclingbox")
    public LockResponse recyclingBox() throws Exception {
        return lockService.recyclingBox();
    }

    /**
     * 入住（FOR 主程序）
     */
    @RequestMapping("/checkin/v1")
    public JSONObject checkinV1(CardInfo cardInfo) throws Exception {
        LockResponse lockResponse = lockService.write(cardInfo.getRoomNo(), LOCK_FORMATTER.format(LocalDateTime.now()), LOCK_FORMATTER.format(TRANSPORT_FORMATTER.parse(cardInfo.getCheckOutTime())), cardInfo.getUserName(), 1, 1, 1, 0, "01", "01");
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, cardInfo, "制卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "制卡失败！" + lockResponse.getMsg());
    }

    /**
     * 退房（FOR 主程序）
     */
    @RequestMapping("/checkout/v1")
    public JSONObject checkoutV1() throws Exception {
        LockResponse lockResponse = lockService.read();
        if (lockResponse.getCode() == 0) {
            ReadCardInfo cardInfo = new ReadCardInfo(lockResponse.getRoomNO(), TRANSPORT_FORMATTER.format(LOCK_FORMATTER.parse(lockResponse.getCheckoutTime())), TRANSPORT_FORMATTER.format(LOCK_FORMATTER.parse(lockResponse.getCheckinTime())));
            return ResultTool.ResultMap(0, cardInfo, "读卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "读卡失败！" + lockResponse.getMsg());
    }

    /**
     * 销卡（FOR 主程序）
     */
    @RequestMapping("/erase/v1")
    public JSONObject eraseV1() throws Exception {
        LockResponse lockResponse = lockService.erase();
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, null, "销卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "销卡失败！" + lockResponse.getMsg());
    }

    /**
     * 续住进卡（FOR 主程序）
     */
    @RequestMapping("/continue/read/v1")
    public JSONObject continueCheckinV1() throws Exception {
        LockResponse lockResponse = lockService.continueRead();
        if (lockResponse.getCode() == 0) {
            ReadCardInfo cardInfo = new ReadCardInfo(lockResponse.getRoomNO(), TRANSPORT_FORMATTER.format(LOCK_FORMATTER.parse(lockResponse.getCheckoutTime())), TRANSPORT_FORMATTER.format(LOCK_FORMATTER.parse(lockResponse.getCheckinTime())));
            return ResultTool.ResultMap(0, cardInfo, "读卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "读卡失败！" + lockResponse.getMsg());
    }

    /**
     * 续住出卡（FOR 主程序）
     */
    @RequestMapping("/continue/write/v1")
    public JSONObject continueWriteV1(CardInfo cardInfo) throws Exception {
        LockResponse lockResponse = lockService.continueWrite(cardInfo.getRoomNo(), LOCK_FORMATTER.format(LocalDateTime.now()), LOCK_FORMATTER.format(TRANSPORT_FORMATTER.parse(cardInfo.getCheckOutTime())), cardInfo.getUserName(), 1, 1, 1, 0, "01", "01");
        if (lockResponse.getCode() == 0) {
            return ResultTool.ResultMap(0, cardInfo, "制卡成功。");
        }
        return ResultTool.ResultMap(lockResponse.getCode(), lockResponse.getMsg(), "制卡失败！" + lockResponse.getMsg());
    }

    /**
     * 卡箱状态（FOR 主程序）
     */
    @RequestMapping("/cardbox/v1")
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
    @RequestMapping("/recyclingbox/v1")
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

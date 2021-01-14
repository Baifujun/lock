package com.chh.lock.controller;

import com.chh.lock.service.LockService;
import com.chh.lock.vo.LockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    @GetMapping("/cardbox")
    public LockResponse cardBox() throws IOException {
        return lockService.cardBox();
    }

    @GetMapping("/recyclingbox")
    public LockResponse recyclingBox() throws IOException {
        return lockService.recyclingBox();
    }

}

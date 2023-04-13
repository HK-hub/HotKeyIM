package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : HK意境
 * @ClassName : ConversationController
 * @date : 2023/3/23 10:52
 * @description : rtc 相关
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/conversation")
public class ConversationController {


    @PostMapping("/room/create")
    public ResponseResult generateRoom() {
        return null;
    }



}

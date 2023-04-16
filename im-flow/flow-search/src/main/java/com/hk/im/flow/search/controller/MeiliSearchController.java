package com.hk.im.flow.search.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.flow.search.service.MessageSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : MeiliSearchController
 * @date : 2023/4/16 21:07
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/search")
public class MeiliSearchController {

    @Resource
    private MessageSearchService messageSearchService;

    /**
     * 全量同步消息
     * @return
     */
    @GetMapping("/full/sync/chat/message")
    public ResponseResult fullSynchronizeChatMessage() {

        return this.messageSearchService.fullSyncMessage();
    }


}

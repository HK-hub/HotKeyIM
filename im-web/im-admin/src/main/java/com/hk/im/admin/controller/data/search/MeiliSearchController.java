package com.hk.im.admin.controller.data.search;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.SearchDocumentRequest;
import com.hk.im.flow.search.service.MessageSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/meili/search")
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

    /**
     * 查询
     * @param request
     * @return
     */
    @PostMapping("/search/chat")
    public ResponseResult search(@RequestBody SearchDocumentRequest request) {

        return this.messageSearchService.searchChatMessage(request);
    }


}

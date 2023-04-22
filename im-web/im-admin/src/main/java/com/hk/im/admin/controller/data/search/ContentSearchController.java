package com.hk.im.admin.controller.data.search;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.flow.search.domain.param.SearchChatContentDomain;
import com.hk.im.flow.search.service.ContentIndexSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : ContentSearchController
 * @date : 2023/4/21 23:14
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/content/search")
public class ContentSearchController {

    @Resource
    private ContentIndexSearchService contentIndexSearchService;


    @GetMapping("/index")
    public ResponseResult searchTalkOrFriendOrGroup(SearchChatContentDomain request) {

        return this.contentIndexSearchService.searchTalkOrFriendOrGroup(request);
    }


}

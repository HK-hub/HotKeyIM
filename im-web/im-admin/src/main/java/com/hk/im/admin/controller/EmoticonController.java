package com.hk.im.admin.controller;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.service.service.EmoticonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : EmoticonController
 * @date : 2023/2/23 16:26
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/emoticon")
public class EmoticonController {

    @Resource
    private EmoticonService emoticonService;


    /**
     * 获取用户的表情包列表：具体指收藏的表情包
     * @param userId
     * @return
     */
    @GetMapping("/user/list")
    public ResponseResult getUserEmoticonList(@RequestParam(value = "userId", required = false) String userId) {

        // 获取当前登录用户
        if (StringUtils.isEmpty(userId)) {
            userId = String.valueOf(UserContextHolder.get().getId());
        }
        return this.emoticonService.getUserEmoticonList(userId);

    }



}

package com.hk.im.admin.controller;

import com.hk.im.client.service.PersonalTagService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.PersonalTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : PersonalTagController
 * @date : 2023/5/20 11:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/personal/tag")
public class PersonalTagController {

    @Resource
    private PersonalTagService personalTagService;


    /**
     * 创建个人标签
     * @param personalTag
     * @return
     */
    @PostMapping("/create")
    public ResponseResult createPersonalTag(@RequestBody PersonalTag personalTag) {

        return this.personalTagService.createPersonalTag(personalTag);
    }

    /**
     * 获取用户个性标签
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getUserPersonalTags() {

        Long userId = UserContextHolder.get().getId();
        List<PersonalTag> userPersonalTags = this.personalTagService.getUserPersonalTags(userId);
        return  ResponseResult.SUCCESS(userPersonalTags);
    }


    /**
     * 删除用户标签
     * @param personalTag
     * @return
     */
    @PostMapping("/delete")
    public ResponseResult deleteUserPersonalTag(@RequestBody PersonalTag personalTag) {

        return this.personalTagService.deleteUserPersonalTag(personalTag);
    }


}

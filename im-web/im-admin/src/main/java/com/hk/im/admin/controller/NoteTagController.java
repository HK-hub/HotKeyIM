package com.hk.im.admin.controller;

import com.hk.im.client.service.TagService;
import com.hk.im.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : NoteTagController
 * @date : 2023/3/27 16:09
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/note/tag")
public class NoteTagController {

    @Resource
    private TagService tagService;


    /**
     * 获取用户笔记列表
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getUserNoteTags(Long userId) {

        return tagService.getUserNoteTags(userId);
    }

}

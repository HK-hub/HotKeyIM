package com.hk.im.admin.controller;

import com.hk.im.client.service.CategoryService;
import com.hk.im.client.service.NoteService;
import com.hk.im.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : NoteController
 * @date : 2023/3/26 14:45
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService noteService;
    @Resource
    private CategoryService categoryService;


    /**
     * 获取用户笔记分类列表
     * @return
     */
    @GetMapping("/category")
    public ResponseResult getNoteCategoryList(Long userId) {

        return this.categoryService.getUserNoteCategoryList(userId);
    }


}

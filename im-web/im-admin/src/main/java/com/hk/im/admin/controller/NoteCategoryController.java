package com.hk.im.admin.controller;

import com.hk.im.client.service.CategoryService;
import com.hk.im.client.service.NoteService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.EditNoteCategoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : NoteCategoryController
 * @date : 2023/3/27 15:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/note/category")
public class NoteCategoryController {

    @Resource
    private NoteService noteService;
    @Resource
    private CategoryService categoryService;


    /**
     * 获取用户笔记分类列表
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getNoteCategoryList(Long userId) {

        return this.categoryService.getUserNoteCategoryList(userId);
    }


    /**
     * 添加或编辑笔记分类
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public ResponseResult editNoteCategoryList(@RequestBody EditNoteCategoryRequest request) {
        return this.categoryService.editNoteCategoryList(request);
    }



}

package com.hk.im.admin.controller;

import com.hk.im.client.service.NoteService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.EditArticleRequest;
import com.hk.im.domain.request.GetArticleListRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询用户笔记文集列表
     * @param request
     * @return
     */
    @GetMapping("/article/list")
    public ResponseResult getArticleList(GetArticleListRequest request) {

        return this.noteService.getNoteArticleList(request);
    }


    @PostMapping("/article/edit")
    public ResponseResult editArticle(EditArticleRequest request) {

        return this.noteService.editNoteArticle(request);
    }



}

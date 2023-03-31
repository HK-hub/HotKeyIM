package com.hk.im.admin.controller;

import com.hk.im.client.service.NoteService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.*;
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


    /**
     * 获取文章笔记具体内容，信息
     * @return
     */
    @GetMapping("/article/detail")
    public ResponseResult getArticleDetail(@RequestParam(name = "noteId") Long noteId) {

        return this.noteService.getArticleDetailById(noteId);
    }


    /**
     * 编辑，新建文章
     * @param request
     * @return
     */
    @PostMapping("/article/edit")
    public ResponseResult editArticle(@RequestBody EditArticleRequest request) {

        log.info("Edit article : {}", request);
        return this.noteService.editNoteArticle(request);
    }


    /**
     * 上传笔记文集图片
     * @param request
     * @return
     */
    @PostMapping("/article/upload/image")
    public  ResponseResult uploadArticleImage(UploadNoteImageRequest request) {

        return this.noteService.uploadNoteImage(request);
    }


    /**
     * 上传笔记文集附件
     * @param request
     * @return
     */
    @PostMapping("/article/upload/annex")
    public  ResponseResult uploadArticle(UploadNoteAnnexRequest request) {

        return this.noteService.uploadNoteAnnex(request);
    }


    /**
     * 移除笔记附件
     * @param request
     * @return
     */
    @PostMapping("/article/annex/delete")
    public ResponseResult deleteArticleAnnex(UploadNoteAnnexRequest request) {

        return nulll;
    }

    /**
     * 收藏，取消收藏星标文集笔记
     * @param request
     * @return
     */
    @PostMapping("/article/asterisk")
    public ResponseResult asteriskArticle(@RequestBody AsteriskArticleRequest request) {

        return this.noteService.asteriskNoteArticle(request);
    }


    /**
     * 给笔记文章设置标签
     * @param request
     * @return
     */
    @PostMapping("/article/tag")
    public  ResponseResult tagArticle(@RequestBody TagArticleRequest request) {

        return this.noteService.tagNoteArticle(request);
    }


    /**
     * 讲笔记文章删除放入回收站
     * @return
     */
    @PostMapping("/article/recycle/put")
    public ResponseResult putRecycleBin(Long articleId) {

        return this.noteService.putNoteToRecycleBin(articleId);
    }


    /**
     * 恢复回收站笔记文章
     * @param articleId
     * @return
     */
    @PostMapping("/article/recycle/recover")
    private ResponseResult recoverRecycleNote(Long articleId) {

        return this.noteService.recoverRecycleNote(articleId);
    }


    /**
     * 查看回收站笔记文章列表
     * @return
     */
    @GetMapping("/article/recycle/list")
    public ResponseResult getRecycleBinNoteList() {
        return this.noteService.getRecycleBinNoteList();
    }


    /**
     * 彻底删除回收站指定笔记文章
     * @return
     */
    @PostMapping("/article/recycle/delete")
    public ResponseResult cleanRecycleBinTheNote(Long articleId) {
        return this.noteService.completelyRemoveNote(articleId);
    }


    /**
     * 清空回收站笔记文章列表
     * @return
     */
    @PostMapping("/article/recycle/clean")
    public ResponseResult cleanRecycleBinNoteList() {

        return this.noteService.cleanRecycleBinNoteList();
    }





}

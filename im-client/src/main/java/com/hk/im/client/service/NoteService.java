package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.im.domain.request.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : HK意境
 * @ClassName : NoteService
 * @date : 2023/3/27 17:22
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface NoteService extends IService<Note> {

    /**
     * 获取用户文集列表
     *
     * @param request
     *
     * @return
     */
    ResponseResult getNoteArticleList(GetArticleListRequest request);

    /**
     * 编辑文章笔记
     *
     * @param request
     *
     * @return
     */
    ResponseResult editNoteArticle(EditArticleRequest request);


    /**
     * 上传笔记文章图片
     * @param request
     *
     * @return
     */
    public ResponseResult uploadNoteImage(UploadNoteImageRequest request);


    /**
     * 获取笔记文章
     *
     * @param noteId
     *
     * @return
     */
    ResponseResult getArticleDetailById(Long noteId);


    /**
     * (取消)收藏笔记
     *
     * @param request
     *
     * @return
     */
    ResponseResult asteriskNoteArticle(AsteriskArticleRequest request);


    /**
     * 设置笔记文章标签
     *
     * @param request
     *
     * @return
     */
    ResponseResult tagNoteArticle(TagArticleRequest request);

    /**
     * 讲笔记文章删除放入回收站
     *
     * @param articleId
     *
     * @return
     */
    ResponseResult putNoteToRecycleBin(Long articleId);

    /**
     * 查看回收站笔记文章
     *
     * @return
     */
    ResponseResult getRecycleBinNoteList();


    /**
     * 彻底删除笔记文章
     *
     * @param articleId
     *
     * @return
     */
    ResponseResult completelyRemoveNote(Long articleId);


    /**
     * 清空笔记文章列表
     *
     * @return
     */
    ResponseResult cleanRecycleBinNoteList();


    /**
     * 恢复回收站文章
     *
     * @param articleId
     *
     * @return
     */
    ResponseResult recoverRecycleNote(Long articleId);

    /**
     * 上传笔记文集附件
     * @param request
     * @return
     */
    ResponseResult uploadNoteAnnex(UploadNoteAnnexRequest request);
}

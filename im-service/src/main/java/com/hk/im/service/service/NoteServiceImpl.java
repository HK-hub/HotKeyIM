package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hk.im.client.service.*;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.*;
import com.hk.im.domain.request.EditArticleRequest;
import com.hk.im.domain.request.GetArticleListRequest;
import com.hk.im.domain.response.EditNoteArticleResponse;
import com.hk.im.domain.vo.NoteVO;
import com.hk.im.infrastructure.mapper.NoteMapper;
import com.hk.im.infrastructure.mapstruct.NoteMapStructure;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : NoteServiceImpl
 * @date : 2023/3/27 16:49
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Resource
    private NoteMapper noteMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;
    @Resource
    private UserCollectionService userCollectionService;
    @Resource
    private NoteTagService noteTagService;

    /**
     * 获取用户文集列表
     *
     * @param request
     *
     * @return {@link java.util.List} of {@link Note}
     */
    @Override
    public ResponseResult getNoteArticleList(GetArticleListRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getPage()) || Objects.isNull(request.getFindType());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL().setDataAsMessage("参数校验失败!");
        }

        if (Objects.isNull(request.getUserId())) {
            request.setUserId(UserContextHolder.get().getId());
        }

        // 素材准备
        Integer findType = request.getFindType();
        Long userId = request.getUserId();
        // 查询基础List集合
        List<Note> noteList = Lists.newArrayList();
        if (findType == 1) {
            // 近期编辑: 两个周
            LocalDate startTime = LocalDate.now().minusDays(14);
            noteList = this.noteMapper.selectRecentEditNoteList(request, startTime);
        } else if (findType == 2) {
            // 我的收藏
            List<UserCollection> collectedNoteList = this.userCollectionService.getUserCollectedNoteList(userId);
            // 检查是否有收藏的
            if (CollectionUtils.isNotEmpty(collectedNoteList)) {
                // 提取笔记id
                List<Long> noteIdList = collectedNoteList.stream().map(UserCollection::getCollectibleId).toList();
                // 获取收藏笔记集合
                noteList = this.noteMapper.selectCollectedNoteList(noteIdList, request);
            }
        } else if (findType == 3) {
            // 笔记分类：传入分类id
            noteList = this.noteMapper.selectNoteListByCategory(request);

        } else if (findType == 4) {
            // 笔记标签: 传入标签id
            // 查询出标签-笔记 映射集合
            List<NoteTag> noteTagList = this.noteTagService.getNoteListByTagId(request.getCid());
            // 检查该标签下是否有笔记
            if (CollectionUtils.isNotEmpty(noteTagList)) {
                // 提取 笔记id
                List<Long> noteIdList = noteTagList.stream().map(NoteTag::getNoteId).toList();
                // 查询标签下笔记集合
                noteList = this.noteMapper.selectNoteListByTag(noteIdList, request);
            }
        }


        // 查询笔记分类，标签信息
        List<NoteVO> noteVOList = noteList.stream().map(note -> {
                    Category category = this.categoryService.getNoteCategory(note.getCategoryId());
                    List<Tag> tagList = this.tagService.getNoteTagList(note.getId());
                    return NoteMapStructure.INSTANCE.toVO(note, category, tagList);
                })
                // 排序：按照最新编辑时间排序
                .sorted(Comparator.comparing(NoteVO::getUpdateTime).reversed())
                .toList();

        // 响应数据
        return ResponseResult.SUCCESS(noteVOList);
    }


    /**
     * 编辑文章笔记
     *
     * @param request
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult editNoteArticle(EditArticleRequest request) {

        // 参数校验
        boolean preCheck = Objects.isNull(request) || Objects.isNull(request.getNoteId()) || Objects.isNull(request.getMdContent())
                || StringUtils.isEmpty(request.getTitle());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("笔记文章信息不完整!");
        }

        // 素材
        Long noteId = request.getNoteId();
        Long userId = UserContextHolder.get().getId();
        Long categoryId = request.getCategoryId();

        // 检查文章是否存在
        Note note = this.getById(noteId);
        String summary = Jsoup.parse(request.getContent())
                .text().substring(0, 20);

        boolean res = false;
        if (Objects.isNull(note)) {
            // 笔记文章不存在，新增文章
            note = new Note()
                    .setAuthorId(userId)
                    .setTitle(request.getTitle())
                    .setStatus(1)
                    .setSummary(summary)
                    .setContent(request.getContent())
                    .setMdContent(request.getMdContent());

            // 确定分类信息
            if (Objects.isNull(categoryId) || 0 == categoryId) {
                // 分类不存在：选择默认分类
                Category defaultCategory = this.categoryService.getNoteDefaultCategory(userId);
                if (Objects.isNull(defaultCategory)) {
                    // 默认分类不存在
                    return ResponseResult.FAIL("抱歉,您未选择文章笔记分类!");
                }

                // 设置分类
                categoryId = defaultCategory.getId();
            }

            note.setCategoryId(categoryId);

            // 保存文章笔记
            res = this.save(note);
        } else {
            // 笔记文章存在：进行更新
            note.setTitle(request.getTitle())
                    .setSummary(summary)
                    .setContent(request.getContent())
                    .setMdContent(request.getMdContent())
                    .setCategoryId(categoryId);
            res = this.updateById(note);
        }

        // 响应数据
        if (BooleanUtils.isFalse(res)) {
            // 保存，编辑失败
            return ResponseResult.FAIL("保存笔记文章失败!");
        }

        // 保存，编辑成功: 构建响应体
        EditNoteArticleResponse result = new EditNoteArticleResponse()
                .setId(note.getId())
                .setTitle(note.getTitle())
                .setCover(note.getCover())
                .setSummary(summary);
        return ResponseResult.SUCCESS(result);
    }


    /**
     * 获取笔记文章
     *
     * @param noteId
     *
     * @return
     */
    @Override
    public ResponseResult getArticleDetailById(Long noteId) {



        return null;
    }
}





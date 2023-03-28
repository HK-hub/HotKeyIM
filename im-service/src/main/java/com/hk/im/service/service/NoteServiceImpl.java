package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hk.im.client.service.*;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.*;
import com.hk.im.domain.request.GetArticleListRequest;
import com.hk.im.domain.vo.NoteVO;
import com.hk.im.infrastructure.mapper.NoteMapper;
import com.hk.im.infrastructure.mapstruct.NoteMapStructure;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName : NoteServiceImpl
 * @author : HK意境
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
     * @param request
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
            // 提取笔记id
            List<Long> noteIdList = collectedNoteList.stream().map(UserCollection::getCollectibleId).toList();
            // 获取收藏笔记集合
            noteList = this.noteMapper.selectCollectedNoteList(noteIdList, request);

        } else if (findType == 3) {
            // 笔记分类：传入分类id
            noteList = this.noteMapper.selectNoteListByCategory(request);

        } else if (findType == 4) {
            // 笔记标签: 传入标签id
            // 查询出标签-笔记 映射集合
            List<NoteTag> noteTagList = this.noteTagService.getNoteListByTagId(request.getCid());
            // 提取 笔记id
            List<Long> noteIdList = noteTagList.stream().map(NoteTag::getNoteId).toList();
            // 查询标签下笔记集合
            noteList = this.noteMapper.selectNoteListByTag(noteIdList, request);
        }


        // 查询笔记分类，标签信息
        List<NoteVO> noteVOList = noteList.stream().map(note -> {
            Category category = this.categoryService.getNoteCategory(note.getId());
            List<Tag> tagList = this.tagService.getNoteTagList(note.getId());
            return NoteMapStructure.INSTANCE.toVO(note, category, tagList);
        }).toList();

        // 响应数据
        return ResponseResult.SUCCESS(noteVOList);
    }
}





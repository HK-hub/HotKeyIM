package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CategoryService;
import com.hk.im.client.service.NoteService;
import com.hk.im.client.service.TagService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Category;
import com.hk.im.domain.entity.Note;
import com.hk.im.domain.entity.Tag;
import com.hk.im.domain.request.GetArticleListRequest;
import com.hk.im.domain.vo.NoteVO;
import com.hk.im.infrastructure.mapper.NoteMapper;
import com.hk.im.infrastructure.mapstruct.NoteMapStructure;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

        // 查询基础List集合
        List<Note> noteList = this.noteMapper.selectNoteArticleList(request);

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





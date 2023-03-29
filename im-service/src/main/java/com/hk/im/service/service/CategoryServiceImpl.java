package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CategoryService;
import com.hk.im.client.service.NoteService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Category;
import com.hk.im.domain.entity.Note;
import com.hk.im.domain.request.EditNoteCategoryRequest;
import com.hk.im.domain.vo.CategoryVO;
import com.hk.im.domain.vo.NoteVO;
import com.hk.im.infrastructure.mapper.CategoryMapper;
import com.hk.im.infrastructure.mapstruct.CategoryMapStructure;
import com.hk.im.infrastructure.mapstruct.NoteMapStructure;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author : HK意境
 * @ClassName : CategoryServiceImpl
 * @date : 2023/2/22 21:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private NoteService noteService;


    /**
     * 获取用户笔记分类列表
     *
     * @param userId
     *
     * @return
     */
    @Override
    public ResponseResult getUserNoteCategoryList(Long userId) {

        // 参数校验
        if (Objects.isNull(userId)) {
            // 未传递，使用登录用户
            userId = UserContextHolder.get().getId();
        }

        List<Category> categories = this.lambdaQuery()
                .eq(Category::getUserId, userId)
                .eq(Category::getDeleted, false)
                .list();

        // 计算分类下文章数量
        List<CategoryVO> categoryVOS = categories.stream().map(category -> {
            // 查询分类对应文章数量
            int count = this.getCategoryNoteCounts(category.getId());
            return CategoryMapStructure.INSTANCE.toVO(category, count);
        })
            // 排序：按照创建时间从大到小排序
            .sorted(Comparator.comparing(CategoryVO::getCreateTime).reversed()).toList();


        return ResponseResult.SUCCESS(categoryVOS);
    }

    /**
     * 添加或修改笔记分类
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult editNoteCategoryList(EditNoteCategoryRequest request) {

        // 参数校验
        log.info("EditNoteCategoryList: {}", request);
        boolean preCheck = Objects.isNull(request) || StringUtils.isEmpty(request.getName()) || Objects.isNull(request.getCategoryId());
        if (BooleanUtils.isTrue(preCheck)) {
            // 参数校验失败
            return ResponseResult.FAIL("参数校验失败!");
        }

        Long userId = UserContextHolder.get().getId();
        boolean res = false;
        Category category = new Category();

        // 查看是否存在分类信息
        boolean exists = this.lambdaQuery()
                .eq(Category::getId, request.getCategoryId())
                .exists();
        if (BooleanUtils.isFalse(exists)) {
            // 分类不存在 -> 保存分类
            category.setUserId(userId)
                    .setName(request.getName())
                    .setDescription(request.getDescription());
            res = this.save(category);

        } else {
            // 分类存在进行更新
            category.setId(request.getCategoryId())
                    .setName(request.getName())
                    .setDescription(request.getDescription());
            res = this.updateById(category);
        }

        // 构造响应结果
        if (BooleanUtils.isFalse(res)) {
            return ResponseResult.FAIL().setDataAsMessage("编辑文集分类失败!");
        }

        return ResponseResult.SUCCESS(category).setMessage("编辑分类信息成功!");
    }


    /**
     * 批量获取笔记的分类列表
     *
     * @param noteList
     *
     * @return
     */
    @Override
    public List<NoteVO> batchGetNoteCategory(List<Note> noteList) {

        List<NoteVO> noteVOList = noteList.stream().map(note -> {
            Category category = this.lambdaQuery().eq(Category::getId, note.getCategoryId())
                    .one();
            return NoteMapStructure.INSTANCE.toVO(note, category, null);
        }).toList();

        return noteVOList;
    }


    /**
     * 获取笔记的分类
     *
     * @param categoryId
     *
     * @return
     */
    @Override
    public Category getNoteCategory(Long categoryId) {
        Category category = this.lambdaQuery().eq(Category::getId, categoryId)
                .one();
        return category;
    }


    /**
     * 获取用户默认笔记分类
     * @param userId
     * @return
     */
    @Override
    public Category getNoteDefaultCategory(Long userId) {

        Category one = this.lambdaQuery()
                .eq(Category::getType, Category.Type.defaulted.ordinal())
                .one();

        return one;
    }


    /**
     * 获取分类下文章数量
     *
     * @param categoryId
     *
     * @return
     */
    public int getCategoryNoteCounts(Long categoryId) {
        Long count = this.noteService.lambdaQuery()
                .eq(Note::getCategoryId, categoryId)
                .count();
        return count.intValue();
    }


}





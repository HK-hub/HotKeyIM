package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.CategoryService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.context.UserContextHolder;
import com.hk.im.domain.entity.Category;
import com.hk.im.infrastructure.mapper.CategoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName : CategoryServiceImpl
 * @author : HK意境
 * @date : 2023/2/22 21:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {


    /**
     * 获取用户笔记分类列表
     * @param userId
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
        return ResponseResult.SUCCESS(categories);
    }
}





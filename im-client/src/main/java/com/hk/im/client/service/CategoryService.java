package com.hk.im.client.service;

import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName : CategoryService
 * @author : HK意境
 * @date : 2023/2/22 21:25
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface CategoryService extends IService<Category> {


    /**
     * 获取用户笔记分类列表
     * @param userId
     * @return
     */
    ResponseResult getUserNoteCategoryList(Long userId);

}

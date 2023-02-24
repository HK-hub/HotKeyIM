package com.hk.im.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.Category;
import com.hk.im.service.service.CategoryService;
import com.hk.im.infrastructure.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

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
    implements CategoryService{

}





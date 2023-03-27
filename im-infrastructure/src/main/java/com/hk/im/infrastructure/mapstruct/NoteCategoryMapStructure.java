package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.bo.NoteCategoryBO;
import com.hk.im.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : NoteCategoryMapStructure
 * @date : 2023/3/26 16:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface NoteCategoryMapStructure {

    public static final NoteCategoryMapStructure INSTANCE = Mappers.getMapper(NoteCategoryMapStructure.class);


    public NoteCategoryBO toBO(Category category);





}

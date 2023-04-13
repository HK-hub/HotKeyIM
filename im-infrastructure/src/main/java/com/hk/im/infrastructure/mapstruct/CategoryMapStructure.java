package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.Category;
import com.hk.im.domain.vo.CategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : CategoryMapStructure
 * @date : 2023/3/28 9:13
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface CategoryMapStructure {

    public static final CategoryMapStructure INSTANCE = Mappers.getMapper(CategoryMapStructure.class);

    public CategoryVO toVO(Category category, Integer count);


}

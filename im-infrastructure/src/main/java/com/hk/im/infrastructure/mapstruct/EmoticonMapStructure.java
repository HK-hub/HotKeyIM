package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.bo.EmoticonBO;
import com.hk.im.domain.entity.CollectEmoticon;
import com.hk.im.domain.entity.Emoticon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : EmoticonMapStructure
 * @date : 2023/2/23 16:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface EmoticonMapStructure {

    public static EmoticonMapStructure INSTANCE = Mappers.getMapper(EmoticonMapStructure.class);

    @Mapping(source = "collectEmoticon.userId", target = "userId")
    @Mapping(source = "emoticon.createTime", target = "createTime")
    @Mapping(source = "emoticon.updateTime", target = "updateTime")
    public EmoticonBO toBO(Emoticon emoticon, CollectEmoticon collectEmoticon);



}

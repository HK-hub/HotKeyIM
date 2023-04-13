package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.Tag;
import com.hk.im.domain.vo.NoteVO;
import com.hk.im.domain.vo.TagVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : NoteTagMapStructure
 * @date : 2023/3/28 10:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface NoteTagMapStructure {

    public static final NoteTagMapStructure INSTANCE = Mappers.getMapper(NoteTagMapStructure.class);

    public TagVO toVO(Tag tag, Integer count);

}

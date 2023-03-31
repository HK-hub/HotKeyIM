package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.Category;
import com.hk.im.domain.entity.Note;
import com.hk.im.domain.entity.NoteAnnex;
import com.hk.im.domain.entity.Tag;
import com.hk.im.domain.vo.NoteDetailVO;
import com.hk.im.domain.vo.NoteVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : NoteMapStructure
 * @date : 2023/3/27 22:04
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface NoteMapStructure {

    public static final NoteMapStructure INSTANCE = Mappers.getMapper(NoteMapStructure.class);


    @Mapping(source = "note.id", target = "id")
    @Mapping(source = "note.deleted", target = "deleted")
    @Mapping(source = "note.createTime", target = "createTime")
    @Mapping(source = "note.updateTime", target = "updateTime")
    public NoteVO toVO(Note note, Category category, List<Tag> tags);

    @Mapping(source = "note.id", target = "id")
    @Mapping(source = "note.deleted", target = "deleted")
    @Mapping(source = "note.createTime", target = "createTime")
    @Mapping(source = "note.updateTime", target = "updateTime")
    public NoteDetailVO toDetailVO(Note note, Category category, List<Tag> tags, List<NoteAnnex> files);



}

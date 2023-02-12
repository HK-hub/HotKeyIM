package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.GroupAnnouncement;
import com.hk.im.domain.vo.GroupAnnouncementVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : HK意境
 * @ClassName : GroupAnnouncementMapStructure
 * @date : 2023/2/12 20:34
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface GroupAnnouncementMapStructure {

    public static final GroupAnnouncementMapStructure INSTANCE = Mappers.getMapper(GroupAnnouncementMapStructure.class);

    public GroupAnnouncementVO toVO(GroupAnnouncement groupAnnouncement);


}

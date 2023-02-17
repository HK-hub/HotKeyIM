package com.hk.im.infrastructure.mapstruct;

import com.hk.im.domain.entity.Group;
import com.hk.im.domain.vo.GroupAnnouncementVO;
import com.hk.im.domain.vo.GroupMemberVO;
import com.hk.im.domain.vo.GroupSettingVO;
import com.hk.im.domain.vo.GroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : HK意境
 * @ClassName : GroupMapStructure
 * @date : 2023/2/12 20:03
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Mapper
public interface GroupMapStructure {

    public static final GroupMapStructure INSTANCE = Mappers.getMapper(GroupMapStructure.class);

    @Mapping(source = "group.groupAccount", target = "groupAccount")
    @Mapping(source = "group.createTime", target = "createTime")
    public GroupVO toVO(Group group, List<GroupMemberVO> member, GroupSettingVO setting,
                        List<GroupAnnouncementVO> announcement);


}




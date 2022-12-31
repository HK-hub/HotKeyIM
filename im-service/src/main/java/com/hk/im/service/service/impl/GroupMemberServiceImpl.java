package com.hk.im.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.domain.entity.GroupMember;
import com.hk.im.infrastructure.mapper.GroupMemberMapper;
import com.hk.im.service.service.GroupMemberService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember>
    implements GroupMemberService {

}





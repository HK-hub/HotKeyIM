package com.hk.im.service.service;

import com.hk.im.client.service.FindService;
import com.hk.im.client.service.GroupService;
import com.hk.im.client.service.GroupSettingService;
import com.hk.im.common.resp.PageResult;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.entity.Group;
import com.hk.im.domain.request.FriendFindRequest;
import com.hk.im.domain.request.UserFindPolicyRequest;
import com.hk.im.domain.vo.GroupSettingVO;
import com.hk.im.domain.vo.GroupVO;
import com.hk.im.infrastructure.mapstruct.GroupMapStructure;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : HK意境
 * @ClassName : FindServiceImpl
 * @date : 2023/2/16 22:32
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Service
public class FindServiceImpl implements FindService {

    @Resource
    private GroupService groupService;
    @Resource
    private GroupSettingService groupSettingService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取公开群聊
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult getPublicGroups(UserFindPolicyRequest request) {

        // 获取缓存的Group 数据
        /*String key = RedisConstants.CACHE_GROUP_KEY + "public:";
        String cacheGroupJsonString = this.stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(cacheGroupJsonString)) {
            // 缓存群聊数据存在
            List<GroupVO> groupVOS = JSON.parseArray(cacheGroupJsonString, GroupVO.class);
            return ResponseResult.SUCCESS(groupVOS);
        }*/

        // 查询公开群组
        request.setOffset((request.getPage() - 1) * request.getSize());
        List<Group> publicGroupList = this.groupService.getPublicGroupList(request);

        // 排序：按照群聊人数，群聊类型，群聊描述进行排序
        Comparator<GroupVO> comparator = Comparator.comparing(GroupVO::getMemberCount).reversed()
                .thenComparing(GroupVO::getGroupType)
                .thenComparing(GroupVO::getCreateTime)
                .thenComparing(GroupVO::getDescription, String.CASE_INSENSITIVE_ORDER);

        // 转换为 VO 对象
        List<GroupVO> groupVOS = publicGroupList.stream()
                .map(group -> {
                    // 查询群聊设置
                    GroupSettingVO groupSetting = this.groupSettingService.getGroupSettingVO(group.getId());
                    return GroupMapStructure.INSTANCE.toVO(group, null, groupSetting, null);
                })
                .sorted(comparator)
                .collect(Collectors.toList());

        // 进行缓存: 公开群聊
        /*this.stringRedisTemplate.opsForValue()
                .set(key, JSON.toJSONString(groupVOS),
                        RedisConstants.CACHE_GROUP_TTL, TimeUnit.MINUTES);*/

        // 响应数据
        PageResult<GroupVO> pageResult = PageResult.of(groupVOS, request.getSize() <= groupVOS.size());
        return ResponseResult.SUCCESS(pageResult);
    }


    /**
     * 查询群聊
     *
     * @param request
     *
     * @return
     */
    @Override
    public ResponseResult searchForGroup(FriendFindRequest request) {

        List<Group> groupList = this.groupService.searchGroupsByKeyword(request);
        // 封装为VO
        List<GroupVO> groupVOS = groupList.stream()
                .map(group -> GroupMapStructure.INSTANCE.toVO(group, null, null, null))
                .collect(Collectors.toList());

        // 响应
        return ResponseResult.SUCCESS(groupVOS);
    }
}

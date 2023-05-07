package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.RobotService;
import com.hk.im.domain.entity.Robot;
import com.hk.im.infrastructure.mapper.RobotMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class RobotServiceImpl extends ServiceImpl<RobotMapper, Robot>
    implements RobotService {

    /**
     * 获取所有可用机器人
     * @return
     */
    @Override
    public List<Robot> getAllEnableRobot() {

        List<Robot> list = this.lambdaQuery()
                .eq(Robot::getDeleted, Boolean.FALSE)
                .eq(Robot::getStatus, 0)
                .eq(Robot::getEnableTalk, Boolean.TRUE)
                .list();

        return list;
    }
}





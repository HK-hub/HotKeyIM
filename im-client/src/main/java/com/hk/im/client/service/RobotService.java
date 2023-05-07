package com.hk.im.client.service;

import com.hk.im.domain.entity.Robot;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface RobotService extends IService<Robot> {

    List<Robot> getAllEnableRobot();

}

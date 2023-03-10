package com.hk.im.service.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.im.client.service.RobotService;
import com.hk.im.domain.entity.Robot;
import com.hk.im.infrastructure.mapper.RobotMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class RobotServiceImpl extends ServiceImpl<RobotMapper, Robot>
    implements RobotService {

}





package com.hk.im.admin.controller.note;

import com.hk.im.client.service.UserZoneService;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.request.zone.GetObservableNotesRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : ZoneController
 * @date : 2023/5/22 11:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/zone")
public class ZoneController {

    @Resource
    private UserZoneService userZoneService;

    /**
     * 获取用户能够查看到的空间说说
     * @return
     */
    @PostMapping("/note/list")
    public ResponseResult getUserObservableNotes(@RequestBody GetObservableNotesRequest request) {

        return this.userZoneService.getObservableNotes(request);
    }

}

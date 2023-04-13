package com.hk.im.admin.controller;

import com.hk.im.client.service.NoteAnnexService;
import com.hk.im.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : NoteAnnexController
 * @date : 2023/3/31 23:00
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/note/annex")
public class NoteAnnexController {

    @Resource
    private NoteAnnexService noteAnnexService;


    /**
     *
     * @return
     */
    @GetMapping("/recover/list")
    public ResponseResult getRecycleBinNoteAnnexList() {

        return this.noteAnnexService.getNoteRecycleAnnexList();
    }


}

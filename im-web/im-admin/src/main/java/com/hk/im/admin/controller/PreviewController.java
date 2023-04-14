package com.hk.im.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.hk.im.common.consntant.RedisConstants;
import com.hk.im.common.resp.ResponseResult;
import com.hk.im.domain.bo.PreviewLinkBO;
import com.hk.im.service.util.UrlLinkPreviewUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/preview")
public class PreviewController {

    // 设置缓存过期时间为 5 分钟
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/url")
    public ResponseResult previewUrl(String url) {

        String key = RedisConstants.CACHE_URL_LINK_KEY + url;
        PreviewLinkBO previewLinkBO = null;
        if (BooleanUtils.isTrue(stringRedisTemplate.hasKey(key))) {
            // 缓存存在：url 的预览信息
            String json = stringRedisTemplate.opsForValue().get(key);
            previewLinkBO = JSON.parseObject(json, PreviewLinkBO.class);

        } else {
            // 预览链接元数据不存在
            try {
                previewLinkBO = UrlLinkPreviewUtil.scrapeLink(url);
            } catch (IOException e) {
                return ResponseResult.FAIL();
            }
            // 设置缓存
            stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(previewLinkBO), RedisConstants.CACHE_URL_LINK_TTL);
        }

        return ResponseResult.SUCCESS(previewLinkBO);
    }


}

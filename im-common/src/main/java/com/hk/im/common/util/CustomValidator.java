package com.hk.im.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : HK意境
 * @ClassName : CustomValidator
 * @date : 2022/12/30 19:44
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CustomValidator {

    // 手机号码
    private static final String PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[0,1,5,6,7,8,9]))\\d{8}$";
    private static final Pattern PATTERN_PHONE_REGEX = Pattern.compile(PHONE_REGEX);

    // 邮箱
    private static final String EMAIL_REGEX = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";

    /**
     * 校验手机号
     * @param phone
     * @return 是否为无效的手机号
     */
    public static boolean invalidMobile(String phone) {
        if (StringUtils.isEmpty(phone) || phone.length() != 11) {
            return true;
        } else {
            return !Pattern.matches(PHONE_REGEX, phone);
        }
    }

    /**
     * 校验邮箱
     * @param email
     * @return 是否未无效的邮箱
     */
    public static boolean invalidEmail(String email) {

        if (StringUtils.isEmpty(email)) {
            return true;
        }

        return !Pattern.matches(EMAIL_REGEX, email);
    }


}

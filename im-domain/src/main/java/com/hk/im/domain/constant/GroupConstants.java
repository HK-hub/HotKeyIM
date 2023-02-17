package com.hk.im.domain.constant;

import com.beust.jcommander.internal.Lists;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hk.im.domain.constant.GroupConstants.GroupCategory.*;

/**
 * @author : HK意境
 * @ClassName : GroupConstants
 * @date : 2023/1/2 16:19
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class GroupConstants {

    /**
     * 群聊分类
     * 群类型:0.未知，1.兴趣爱好，2.行业交流，3.生活休闲，3.学习考试，4.娱乐游戏，5.置业安家，6.品牌产品，7.粉丝，8.同学同事，9.家校师生
     */
    @Getter
    public static enum GroupCategory {
        DEFAULT("unknown"),
        INTERESTING("interesting"),
        INDUSTRY("industry"),
        LIFE_AND_LEISURE("life"),
        LEARN_AND_EXAM("learn"),
        ENTERTAINMENT("entertainment"),
        HOMELAND("homeland"),
        BRAND_PRODUCT("brand product"),
        FANS("fans"),
        CLASSMATES_AND_COLLEAGUES("classmates and colleagues"),
        PARENTS_CAMPUS_TEACHER_STUDENT("teacher and student")
        ;
        private String category;

        GroupCategory(String category) {
            this.category = category;
        }
    }

    /**
     * 加群方式：1.允许任何人everybody,2.需要验证verification, 3.回答问题，4.不允许人加群nobody,
     */
    public static enum GroupJoinType {
        DEFAULT,
        // 允许任何人
        EVERYBODY,
        // 需要验证
        VERIFICATION,
        // 回答问题
        ANSWER,
        // 不允许任何人加群
        NOBODY,
    }

    public static enum GroupFindType {

        DEFAULT,
        // 发现群方式：1.公开群(支持搜索群名称，群号，群二维码，邀请)，
        PUBLIC,
        // 2.不公开群(不支持搜索群名称，支持搜索群号，群二维码，邀请)，
        PRIVATE,
        // 3.邀请制(只能通过成员邀请)
        INVITE,
    }

    /**
     * 获取群聊类型
     * @param category
     * @return
     */
    public static GroupCategory getGroupCategory(String category) {

        GroupCategory type = switch (category) {
            case "未知" -> DEFAULT;
            case "兴趣爱好" -> INTERESTING;
            case "行业交流" -> INDUSTRY;
            case "生活休闲" -> LIFE_AND_LEISURE;
            case "学习考试" -> LEARN_AND_EXAM;
            case "娱乐游戏" -> ENTERTAINMENT;
            case "品牌产品" -> BRAND_PRODUCT;
            case "粉丝" -> FANS;
            case "同学同事" -> CLASSMATES_AND_COLLEAGUES;
            case "家校师生" -> PARENTS_CAMPUS_TEACHER_STUDENT;
            default -> DEFAULT;
        };

        return type;
    }


    // 群聊类型
    // 群类型:0.未知，1.兴趣爱好，2.行业交流，3.生活休闲，3.学习考试，4.娱乐游戏，5.置业安家，6.品牌产品，7.粉丝，8.同学同事，9.家校师生
    public static final String[] groupCategory = {"兴趣爱好", "行业交流","生活休闲", "学习考试", "娱乐游戏", "置业安家", "品牌产品", "粉丝", "同学同事", "家校师生"};

    public static List<Integer> getMatchGroupCategory(String keyword) {
        ArrayList<Integer> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(keyword);
        for (int i = 0; i < groupCategory.length; i++) {
            String category = groupCategory[i];
            Matcher matcher = pattern.matcher(category);
            if (matcher.matches()) {
                list.add(i);
            }
        }
        return list;
    }


}

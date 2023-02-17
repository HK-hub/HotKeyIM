package com.hk.im.admin.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class EmptyListPlugin implements InnerInterceptor {

    private static final Pattern PATTERN = Pattern.compile("[\"|'](.*?)[\"|']");

    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        return !hasEmptyList(boundSql.getSql());
    }

    /**
     * 判断是否存在空list
     */
    private static boolean hasEmptyList(String sql) {
        sql = removeInterference(sql);
        List<String> keywordList = Lists.newArrayList("in", "IN");
        boolean hasEmptyList = Boolean.FALSE;
        for (String keyword : keywordList) {
            List<String> matcherList = Lists.newArrayList();
            // 获取关键词，关键词前必须为空白字符，但以关键词开头的单词也会被匹配
            // 匹配以" in (" 开头的，")"结尾的最短字符串
            Pattern pattern = Pattern.compile("(?<=\\s" + keyword + "\\s{0,10}\\().*?(?=\\))");
            Matcher matcher = pattern.matcher(sql);
            while (matcher.find()) {
                matcherList.add(matcher.group());
            }
            if (CollectionUtils.isNotEmpty(matcherList)) {
                hasEmptyList = checkEmptyList(matcherList);
                if (hasEmptyList) {
                    break;
                }
            }
        }
        return hasEmptyList;
    }

    /**
     * 去除字符中的干扰项，避免字符串中的内容干扰判断。
     */
    private static String removeInterference(String sql) {
        Matcher matcher = PATTERN.matcher(sql);
        while (matcher.find()) {
            String replaceWord = matcher.group();
            sql = sql.replace(replaceWord, "''");
        }
        return sql;
    }

    /**
     * 校验是否有空的字符串
     */
    private static boolean checkEmptyList(List<String> matcherList) {
        boolean isHaveEmptyList = Boolean.FALSE;
        // 获取()内的内容
        for (String subSql : matcherList) {
            // 如果关键词之后无任何sql语句，则sql语句结尾为关键词，此时判定为空列表
            if (StringUtils.isBlank(subSql)) {
                isHaveEmptyList = Boolean.TRUE;
                break;
            }
        }
        return isHaveEmptyList;
    }
}
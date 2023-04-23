package com.hk.im.domain.annotation;

import java.util.ArrayList;
import java.util.List;

/***
 * java 数据类型换 redis search 数据类型
 * 目前我只知道两种数据类型
 */
public enum FieldType {

    STRING,NUMERIC,DATE_TIME, AUTO;


    private static List<String> StringParameterType = new ArrayList<>() {{
        add("java.lang.String");
        add("java.lang.Char");
        add("java.time.LocalDateTime");
        add("Char");
    }};

    public static FieldType getParameterType(String parameterTypeName){
        if(StringParameterType.contains(parameterTypeName)){
            return FieldType.STRING;
        }else{
            return FieldType.NUMERIC;
        }
    }
}
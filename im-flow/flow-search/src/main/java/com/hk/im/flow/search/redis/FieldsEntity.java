package com.hk.im.flow.search.redis;

import com.hk.im.domain.annotation.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldsEntity {

    FieldType fieldType;
    String fieldName;

}
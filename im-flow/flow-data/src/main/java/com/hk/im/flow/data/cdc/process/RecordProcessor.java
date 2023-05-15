package com.hk.im.flow.data.cdc.process;

import com.meilisearch.sdk.exceptions.MeilisearchException;

/**
 * @author : HK意境
 * @ClassName : RecordProcessor
 * @date : 2023/5/15 19:02
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public interface RecordProcessor<T> {

    public T create(T t);

    public T update(T before, T after) throws MeilisearchException;

    public T remove(T before, T after);

}

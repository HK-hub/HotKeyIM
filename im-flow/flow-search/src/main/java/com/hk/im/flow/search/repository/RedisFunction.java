package com.hk.im.flow.search.repository;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 此类定义 是为了 让 方法可以序列化
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface RedisFunction<T, R> extends Function<T, R>, Serializable {
}
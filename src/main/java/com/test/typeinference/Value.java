package com.test.typeinference;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Created by Matija Vižintin
 * Date: 09. 05. 2015
 * Time: 09.45
 */
public class Value<T> {
    public static <T> T defaultValue() {
        return null;
    }

    public T getValueOrDefault(T value, T defaultValue) {
        return MoreObjects.firstNonNull(value, defaultValue);
    }
}

package com.test.functional;

/**
 * Created by Matija Vižintin
 * Date: 21. 05. 2015
 * Time: 20.44
 */
@FunctionalInterface
public interface Converter<F, T> {

    T convert(F from);
}

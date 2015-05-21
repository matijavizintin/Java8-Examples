package com.test.defaultmethods;

import java.util.function.Supplier;

/**
 * Created by Matija Vižintin
 * Date: 08. 05. 2015
 * Time: 16.56
 */
public interface DefaultableInterface {
    static DefaultInterface createStatic(Supplier<DefaultInterface> supplier) {
        return supplier.get();
    }
}

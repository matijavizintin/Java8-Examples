package com.test.crappy;

import com.test.typeinference.Value;
import org.junit.Test;

/**
 * Created by Matija Vižintin
 * Date: 09. 05. 2015
 * Time: 09.47
 */
public class TypeInferenceTest {

    @Test
    public void test1() {
        Value<String> value = new Value<>();
        value.getValueOrDefault("25", Value.defaultValue());
    }
}

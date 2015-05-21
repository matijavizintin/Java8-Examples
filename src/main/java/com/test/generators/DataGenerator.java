package com.test.generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matija Vižintin
 * Date: 16. 05. 2015
 * Time: 14.20
 */
public class DataGenerator {

    public static List<String> strings(int stringSize, int number) {

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            strings.add(RandomStringUtils.randomAlphabetic(stringSize));
        }
        return strings;
    }
}

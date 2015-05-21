package com.test;

import org.junit.Test;

/**
 * Created by Matija Vižintin
 * Date: 08. 05. 2015
 * Time: 19.11
 */
public class ThreadsTest {
    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            new Thread(
                    () -> {
                        Long x = 1L;
                        for (long j = 1; j < 1000000000; j++) {
                            x += j;
                        }
                        Long y = x - 100;
                        System.out.println("y = " + y);
                    }).start();
        }
    }
}

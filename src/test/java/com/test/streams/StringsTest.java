package com.test.streams;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Matija Vižintin
 * Date: 28. 08. 2015
 * Time: 13.18
 */
public class StringsTest extends LoggingTimedTest {

    /**
     * This test shows simple join operation on strings.
     */
    @Test
    public void joining() {
        String joined = String.join(":", "a", "b", "c", "d");
        Assert.assertEquals("a:b:c:d", joined);
    }
}

package com.test.timed;

import com.google.common.base.Stopwatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matija Vižintin
 * Date: 30. 05. 2015
 * Time: 19.42
 */
public abstract class LoggingTimedTest {
    private Stopwatch stopwatch;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        stopwatch = Stopwatch.createStarted();
        System.out.printf("\n>>> Starting test %s <<<\n", testName.getMethodName());
        System.out.println("--------------------------------");
    }

    @After
    public void after() {
        long execution = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        System.out.println("--------------------------------");
        System.out.printf(">>> Test %s finished. Execution took %d milliseconds <<<\n", testName.getMethodName(), execution);
    }
}

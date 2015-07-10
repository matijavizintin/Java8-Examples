package com.test.concurrency;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matija Vižintin
 * Date: 19. 06. 2015
 * Time: 13.10
 */
public class ScheduledExecutorsTest extends LoggingTimedTest {

    /**
     * This test show how to schedule a non-repetitive task started with a delay.
     */
    private long counter = 0;
    @Test(timeout = 3100)
    public void startWihDelay() throws InterruptedException {
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // schedule task in 3 second
        ScheduledFuture sf = scheduledService.schedule(() -> counter++, 3, TimeUnit.SECONDS);

        // sleep for 1 second and check remaining time to execution time
        Thread.sleep(1000);
        long remaining = sf.getDelay(TimeUnit.MILLISECONDS);
        Assert.assertTrue(1900 < remaining && remaining < 2100);

        // wait for the task to be scheduled and shutdown scheduled service
        Thread.sleep(remaining + 10);
        scheduledService.shutdown();
        Assert.assertEquals("Task should be executed exactly once.", 1L, counter);
    }

    /**
     * This test shows how tasks can be scheduled at fixed rate (task is executed every N time units, ignoring the task duration) or with fixed delay
     * (task is executed every N time units, taking into account the task duration - the next cycle starts when task is finished).
     */
    private long executionCounter = 0;
    @Test(timeout = 20200)
    public void schedulePeriodically() throws InterruptedException {
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // runnable
        Runnable runnable = () -> {
            System.out.printf(
                    "%s Executing scheduled task on thread %s. Exec count [%d]\n", Clock.systemUTC().instant(), Thread.currentThread().getName(),
                    executionCounter + 1);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.printf("%s Interruption exception in thread %s.\n", Clock.systemUTC().instant(), Thread.currentThread().getName());
                return;
            }
            System.out.printf(
                    "%s Scheduled task finished on thread %s. Exec count[%d]\n", Clock.systemUTC().instant(), Thread.currentThread().getName(),
                    executionCounter + 1);
            executionCounter++;
        };

        // ******** FIXED RATE *************
        // schedule repetitive task at fixed rate every 2 seconds with duration 1 seconds
        executionCounter = 0;
        long period = 2000;
        System.out.println("Starting scheduler at fixed rate.");
        scheduledService.scheduleAtFixedRate(runnable, 10, period, TimeUnit.MILLISECONDS);

        // run scheduler for 10 seconds
        TimeUnit.SECONDS.sleep(10);
        scheduledService.shutdownNow();
        System.out.println("Scheduler at fixed rate competed.\n");

        // assert that there were 5 tasks executed (5 * 2 second delay <= 10 seconds)
        Assert.assertEquals(5, executionCounter);

        // ******** FIXED DELAY *************
        // crate new scheduler service
        scheduledService = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // schedule repetitive task with fixed delay every 2 seconds with duration 1 second
        executionCounter = 0;
        System.out.println("Starting scheduler with fixed delay.");
        scheduledService.scheduleWithFixedDelay(runnable, 10, period, TimeUnit.MILLISECONDS);

        // run scheduler for 10 seconds
        TimeUnit.SECONDS.sleep(10);
        scheduledService.shutdownNow();
        System.out.println("Scheduler with fixed delay competed.\n");

        // assert that there were 3 tasks executed (3 * (2 second delay + 1 second execution) <= 10 seconds)
        Assert.assertEquals(3, executionCounter);
    }

    /**
     * This tests shows that only one executor runs at a time no matter how many threads we have in the pool. If we have an scheduled executor every
     * 1 second and a task takes 2 seconds then the next executor is started after the first terminates, so no concurrent execution.
     */
    @Test
    public void oneExecutionThreadAtTime() throws InterruptedException {
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // runnable
        Runnable runnable = () -> {
            System.out.printf(
                    "%s Executing scheduled task on thread %s. Exec count [%d]\n", Clock.systemUTC().instant(), Thread.currentThread().getName(),
                    executionCounter + 1);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                System.out.printf("%s Interruption exception in thread %s.\n", Clock.systemUTC().instant(), Thread.currentThread().getName());
                return;
            }
            System.out.printf(
                    "%s Scheduled task finished on thread %s. Exec count[%d]\n", Clock.systemUTC().instant(), Thread.currentThread().getName(),
                    executionCounter + 1);
            executionCounter++;
        };

        // ******** FIXED RATE *************
        // schedule repetitive task at fixed rate every 1 seconds with duration 2 seconds
        executionCounter = 0;
        long period = 1000;
        System.out.println("Starting scheduler at fixed rate.");
        scheduledService.scheduleAtFixedRate(runnable, 10, period, TimeUnit.MILLISECONDS);

        // run scheduler for 10 seconds
        TimeUnit.SECONDS.sleep(10);
        scheduledService.shutdownNow();
        System.out.println("Scheduler at fixed rate competed.\n");

        // assert that there were 5 tasks executed because of serial execution (1s delay + 4 * 2 seconds per task <= 10 seconds)
        // NOTE: if tasks were executed concurrently then there will be 10 executions
        Assert.assertEquals(4, executionCounter);
    }
}

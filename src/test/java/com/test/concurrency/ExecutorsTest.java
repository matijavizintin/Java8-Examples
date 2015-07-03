package com.test.concurrency;

import com.google.common.collect.ImmutableList;
import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Matija Vižintin
 * Date: 12. 06. 2015
 * Time: 12.25
 */
public class ExecutorsTest extends LoggingTimedTest {

    /**
     * This is a single thread executor test that shows how tasks are executed serially.
     */
    @Test(timeout = 5100)
    public void singleThreadExecutor() {
        ExecutorService service = Executors.newSingleThreadExecutor();

        // prepare task
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // pass
            }
            System.out.printf("Task in thread %s done.\n\n", Thread.currentThread().getName());
        };

        // execute multiple tasks
        service.submit(task);
        service.submit(task);
        service.submit(task);

        System.out.println("Tasks were submitted.");

        // wait for executor to finish; if test's thread dies then tasks are not executed (NOTE: main thread doesn't die, executor has to be shutdown)
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // pass
        }

        // shutdown the executor otherwise it will be listening for new tasks
        service.shutdownNow();
        System.out.println("Executor shut down.");
    }

    /**
     * Method shows how executors can be shut down gracefully. It sends the shutdown signal and sets maximum time to wait for the tasks to be
     * completed. After the timeout, tasks are interrupted.
     */
    @Test(timeout = 5100)
    public void executorShutdown() throws InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();

        service.submit(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        // pass
                    }
                });

        // send shutdown signal to the executor - it will wait for the task to complete
        service.shutdown();

        // set max time to wait - then exception will be thrown
        service.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * This test starts a task that returns a result. The executor returns a hook which can be used to retrieve the result.
     */
    @Test(timeout = 4100, expected = TimeoutException.class)
    public void executeCallable() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService service = Executors.newCachedThreadPool();

        // create callable
        Callable<Integer> task = () -> {
            System.out.printf("%s: Sleeping for 3 seconds.\n", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            System.out.printf("%s: Sleeping ended after 3 seconds. Returning result.\n", Thread.currentThread().getName());
            return 10;
        };

        // execute task and get a hook to the result
        Future<Integer> future = service.submit(task);

        // get result
        System.out.printf("%s: Waiting for result.\n", Thread.currentThread().getName());
        Integer result = future.get();
        System.out.printf("%s: Result retrieved: %d\n", Thread.currentThread().getName(), result);

        // assert
        Assert.assertEquals(10, result.intValue());

        // execute another task but wait for the result less than 3 seconds
        future = service.submit(task);
        future.get(1, TimeUnit.SECONDS);

        // get should timeout and throw an exception
        Assert.assertTrue(false);
    }

    /**
     * This method shows what happens if the executor is shutdown before the result is retrieved.
     */
    @Test(expected = ExecutionException.class, timeout = 150)
    public void executeShutdown2() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        // create callable
        Callable<Integer> task = () -> {
            TimeUnit.SECONDS.sleep(1);
            return -1;
        };

        // execute task and get a hook to the result
        Future<Integer> future = service.submit(task);

        // shutdown the executor and then retrieve the result
        System.out.println("Shutdown the executor.");
        service.shutdownNow();
        System.out.println("Executor shutdown.");
        System.out.println("Trying to retrieve the value.");
        future.get();

        // value can't be retrieved because the executor was terminated previously
        Assert.assertTrue(false);
    }

    /**
     * This test show how can multiple jobs can be submitted at once. InvokeAll starts all tasks and returns a list of future that are used as hooks
     * to execution result.
     */
    private Set<Long> usedThreads = new HashSet<>();
    @Test
    public void invokeAll() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();

        // crate callable
        usedThreads.clear();
        Callable<Long> future = () -> {
            usedThreads.add(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getName());
            return Thread.currentThread().getId();
        };

        // batch execute 4 tasks
        List<Future<Long>> hooks = service.invokeAll(ImmutableList.of(future, future, future, future));

        // use invoke all response as stream
        long tasksExecuted = hooks.stream().count();
        Assert.assertEquals(4, tasksExecuted);

        // assert no of threads used
        Assert.assertEquals(4, usedThreads.size());
        service.shutdown();
    }
}

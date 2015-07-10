package com.test.concurrency;

import com.test.timed.LoggingTimedTest;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

/**
 * Created by Matija Vižintin
 * Date: 10. 07. 2015
 * Time: 13.16
 */
public class SynchronizationTest extends LoggingTimedTest {

    private int counter = 0;

    private void increment() {
        counter++;
    }

    private synchronized void incrementSynchronized() {
        counter++;
    }

    /**
     * This test shows that when incrementing a non-synchronized counter in multiple threads race conditions occur. Counter value is expected to be
     * less than number of incrementations.
     *
     * @throws InterruptedException
     */
    @Test
    public void unSynchronizedIncrement() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // increment counter max times
        counter = 0;
        int max = 10000;
        IntStream.range(0, max).forEach(value -> service.submit(this::increment));

        // wait for a second and shutdown executor
        TimeUnit.SECONDS.sleep(1);
        service.shutdown();
        System.out.printf("Counter value: %d\n", counter);

        // assert that counter < max due to race conditions
        Assert.assertTrue(counter < max);
    }

    /**
     * Synchronized incrementation test. The same as upper test except increment call is synchronized. Counter is expected to be the same as number of
     * incrementations.
     *
     * @throws InterruptedException
     */
    @Test
    public void synchronizedIncrement() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // increment counter max times
        counter = 0;
        int max = 10000;
        IntStream.range(0, max).forEach(value -> service.submit(this::incrementSynchronized));

        // wait for a second and shutdown executor
        TimeUnit.SECONDS.sleep(1);
        service.shutdown();
        System.out.printf("Counter value: %d\n", counter);

        // assert that counter == max
        Assert.assertEquals(max, counter);
    }

    /**
     * This test shows that read lock allows concurrent execution if there is no write lock. First task hold a write lock for 1 second, then the
     * other two tasks are executed in parallel with no waiting.
     *
     * @throws InterruptedException
     */
    @Test(timeout = 2150)
    public void readWriteLock() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        Map<String, String> map = new HashMap<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();

        // write task
        service.submit(
                () -> {
                    lock.writeLock().lock();
                    System.out.printf("%s Write lock acquired. Thread name: %s\n", Clock.systemUTC().instant(), Thread.currentThread().getName());
                    try {
                        TimeUnit.SECONDS.sleep(1);      // sleep for 1 second
                        map.put("dummy", "dummy");      // put into map
                        System.out.printf("%s Value written.\n", Clock.systemUTC().instant());
                    } catch (Exception e) {
                        // pass
                    } finally {
                        lock.writeLock().unlock();
                        System.out.printf("%s Write lock released. Thread name: %s\n", Clock.systemUTC().instant(), Thread.currentThread().getName());
                    }
                });

        // read tasks
        Runnable readTask = () -> {
            lock.readLock().lock();
            System.out.printf("%s Read lock acquired. Thread name: %s\n", Clock.systemUTC().instant(), Thread.currentThread().getName());
            try {
                System.out.printf("%s Value: %s\n", Clock.systemUTC().instant(), map.get("dummy"));       // print result
                TimeUnit.SECONDS.sleep(1);                  // hold read lock for 1 more second
            } catch (Exception e) {
                // pass
            } finally {
                lock.readLock().unlock();
                System.out.printf("%s Read lock released. Thread name: %s\n", Clock.systemUTC().instant(), Thread.currentThread().getName());
            }
        };
        service.submit(readTask);
        service.submit(readTask);

        // shutdown executor
        service.awaitTermination(2050, TimeUnit.MILLISECONDS);
    }

    /**
     * Optimistic locking example. Optimistic lock is a non-blocking lock so doesn't prevent write lock to be acquired. When a write lock is acquired
     * optimistic lock is no longer valid. Note than even when write lock is released optimistic lock is still invalid.
     *
     *
     * @throws InterruptedException
     */
    @Test(timeout = 2150)
    public void stampedLock() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // optimistic locking
        StampedLock lock = new StampedLock();
        service.submit(() -> {
                          long stamp = lock.tryOptimisticRead();
                           System.out.printf("%s Trying optimistic read lock\n", Clock.systemUTC().instant());
                          try {
                              System.out.printf("%s Read lock acquired: %b\n", Clock.systemUTC().instant(), lock.validate(stamp));
                              TimeUnit.SECONDS.sleep(1);
                              System.out.printf("%s Read lock acquired: %b\n", Clock.systemUTC().instant(), lock.validate(stamp));
                              TimeUnit.SECONDS.sleep(1);
                              System.out.printf("%s Read lock acquired: %b\n", Clock.systemUTC().instant(), lock.validate(stamp));
                          } catch (Exception e) {
                              // pass
                          } finally {
                              lock.unlock(stamp);
                              System.out.printf("%s Optimistic lock released\n", Clock.systemUTC().instant());
                          }
                       });

        // write lock
        service.submit(() -> {
                          long stamp = lock.writeLock();
                           System.out.printf("%s Write lock acquired\n", Clock.systemUTC().instant());
                          try {
                              TimeUnit.SECONDS.sleep(1);
                          } catch (Exception e) {
                              // pass
                          } finally {
                              lock.unlock(stamp);
                              System.out.printf("%s Write lock released\n", Clock.systemUTC().instant());
                          }
                       });

        // shutdown
        service.awaitTermination(2050, TimeUnit.MILLISECONDS);
    }

    @Test
    public void convertReadToWriteLock() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(ForkJoinTest.NO_OF_VCORES);

        // optimistic locking
        StampedLock lock = new StampedLock();

        // TODO: ...
    }
}

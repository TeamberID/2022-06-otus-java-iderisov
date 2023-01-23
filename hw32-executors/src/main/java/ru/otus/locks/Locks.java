package ru.otus.locks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {

    private int counter;
    private boolean isAsc = true;

    private static final Logger logger = LoggerFactory.getLogger(Locks.class);

    public static void main(String[] args) {
        new Locks().work();
    }

    public void work() {

        final Lock monitor = new ReentrantLock();
        final Condition condition = monitor.newCondition();
        final AtomicBoolean firstReady = new AtomicBoolean(true);
        final AtomicBoolean secondReady = new AtomicBoolean(false);

        Thread firstThread = new Thread() {
            @Override
            public void run() {
                setName("thread1");
                while (true) {
                    monitor.lock();
                    try {
                        while (!firstReady.get()) {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        logger.info(String.valueOf(counter));
                        sleepOneSec();

                        secondReady.set(true);
                        firstReady.set(false);
                        condition.signalAll();
                    } finally {
                        monitor.unlock();
                    }
                }
            }
        };

        Thread secondThread = new Thread() {
            @Override
            public void run() {
                setName("thread2");
                while (true) {
                    monitor.lock();
                    try {
                        while (!secondReady.get()) {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        logger.info(String.valueOf(counter));
                        calculate();
                        sleepOneSec();

                        firstReady.set(true);
                        secondReady.set(false);
                        condition.signalAll();
                    } finally {
                        monitor.unlock();
                    }

                }
            }
        };

        try {
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static void sleepOneSec() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void calculate() {
        if (counter == 10) {
            isAsc = false;
        } else if (counter == 1) {
            isAsc = true;
        }

        if (isAsc) {
            counter++;
        } else {
            counter--;
        }
    }
}
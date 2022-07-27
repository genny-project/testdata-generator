package life.genny.datagenerator.utils;

import life.genny.datagenerator.ApplicationStartup;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class MyThreadPool {
    private static final Logger LOGGER = Logger.getLogger(MyThreadPool.class);
    private final List<MyThread> threads = new ArrayList<>();
    private final List<PoolTask> pendingTasks = new ArrayList<>();

    public MyThreadPool(int maxThread) {
        int max = Math.min(Math.max(maxThread, 1), 20);
        for (int i = 0; i < max; i++) {
            threads.add(new MyThread(i));
        }
    }

    public void add(PoolTask task) {
        boolean found = false;
        for (MyThread thread : threads) {
            if (!thread.busy) {
                found = true;
                LOGGER.info("thread executed");
                thread.execute(task, onComplete);
            }
        }
        if (!found) {
            LOGGER.info("thread added to pending:" + pendingTasks.size());
            pendingTasks.add(task);
        }
    }

    private final OnComplete onComplete = (thread) -> {
        LOGGER.info("thread complete: " + thread.id);
        if (!pendingTasks.isEmpty()) {
            LOGGER.info("thread took from pending:" + pendingTasks.size());
            thread.execute(pendingTasks.remove(0), this.onComplete);
        }
    };

    public interface PoolTask {
        void run();
    }

    private interface OnComplete {
        void onComplete(MyThread thread);
    }

    public static class MyThread {
        final int id;
        private static final Logger LOGGER = Logger.getLogger(ApplicationStartup.class);
        private boolean busy = false;
        private Thread thread;

        public MyThread(int id) {
            this.id = id;
        }

        public boolean isBusy() {
            return thread != null &&
                    thread.isAlive();
        }

        public void execute(PoolTask task, OnComplete complete) {
            LOGGER.info("Thread started: " + id);
            busy = true;
            thread = new Thread(() -> {
                try {
                    Thread.sleep(300);
                    task.run();
                    Thread.sleep(300);
                } catch (Throwable e) {
                    LOGGER.error(e);
                }
                busy = false;
                complete.onComplete(this);
            });
            thread.start();
        }

    }
}

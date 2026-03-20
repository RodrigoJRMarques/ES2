package object_pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FormatterPool {

    private static final int MAX = 5;

    private BlockingQueue<LogFormatter> pool = new LinkedBlockingQueue<>();

    private static volatile FormatterPool instance;

    private FormatterPool() {}

    // Singleton
    public static FormatterPool getInstance() {
        if (instance == null) {
            synchronized (FormatterPool.class) {
                if (instance == null) {
                    instance = new FormatterPool();
                }
            }
        }
        return instance;
    }

    public LogFormatter acquire() {

        LogFormatter formatter = pool.poll();

        if (formatter == null && pool.size() < MAX) {
            return new LogFormatter();
        }

        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void release(LogFormatter formatter) {
        pool.offer(formatter);
    }
}

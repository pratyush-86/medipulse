package com.medipulse.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * MULTITHREADING: events are written to the activity log by a pool of
 * background worker threads, so web requests never wait for logging.
 * The log itself is a thread-safe ConcurrentLinkedDeque.
 */
public class NotificationService {
    private static final NotificationService INSTANCE = new NotificationService();
    private static final int MAX_LOG = 25;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ExecutorService pool = Executors.newFixedThreadPool(2);
    private final ConcurrentLinkedDeque<String> log = new ConcurrentLinkedDeque<>();

    private NotificationService() { }

    public static NotificationService getInstance() {
        return INSTANCE;
    }

    public void notifyAsync(final String message) {
        pool.submit(() -> {
            String entry = LocalTime.now().format(FMT) + "  [" + Thread.currentThread().getName() + "]  " + message;
            log.addFirst(entry);
            while (log.size() > MAX_LOG) {
                log.pollLast();
            }
        });
    }

    public List<String> recent() {
        return new ArrayList<>(log);
    }

    public void shutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(3, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

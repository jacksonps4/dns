package com.minorityhobbies.dns.service;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CorrelatingRequester<T, R> implements AutoCloseable {
    private final ExecutorService threads = Executors.newCachedThreadPool();
    private final Map<T, CompletableFuture<R>> activeRequests = new ConcurrentHashMap<>();
    private final Consumer<T> sender;
    private final Supplier<R> receiver;

    public CorrelatingRequester(Consumer<T> sender, Supplier<R> receiver) {
        this.sender = sender;
        this.receiver = receiver;
        threads.submit(this::processResponses);
    }

    public R sendMessage(T req) throws TimeoutException {
        CompletableFuture<R> responder = new CompletableFuture<>();
        threads.submit(() -> sendRequest(req, responder));
        try {
            return responder.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    void sendRequest(T request, CompletableFuture<R> responder) {
        // store request
        activeRequests.put(request, responder);

        // send request
        sender.accept(request);
    }

    void processResponses() {
        while (!Thread.currentThread().isInterrupted()) {
            R r = receiver.get();

            CompletableFuture<R> responder = activeRequests.remove(r);
            if (responder != null) {
                responder.complete(r);
            }
        }
    }

    @Override
    public void close() throws Exception {
        threads.shutdownNow();
    }
}

package dev.kurumiDisciples.javadex.api.requests.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RestAction<T> {

    private final Supplier<T> supplier;
    private final Consumer<T> successHandler;
    private final Consumer<Throwable> failureHandler;

    private RestAction(Supplier<T> supplier, Consumer<T> successHandler, Consumer<Throwable> failureHandler) {
        this.supplier = supplier;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    public static <T> RestAction<T> of(Supplier<T> supplier, Consumer<T> successHandler, Consumer<Throwable> failureHandler) {
        return new RestAction<>(supplier, successHandler, failureHandler);
    }

    public void complete() {
        try {
            T result = supplier.get();
            successHandler.accept(result);
        } catch (Throwable t) {
            failureHandler.accept(t);
        }
    }

    public RestAction<T> queue() {
        // Wrap the current RestAction in a new RestAction that executes on a new thread
        return of(
            supplier,
            result -> new Thread(() -> successHandler.accept(result)).start(),
            error -> new Thread(() -> failureHandler.accept(error)).start()
        );
    }

    public CompletableFuture<T> submit() {
        CompletableFuture<T> future = new CompletableFuture<>();
        RestAction<T> restAction = of(
            supplier,
            result -> future.complete(result),
            error -> future.completeExceptionally(error)
        );
        restAction.queue();
        return future;
    }

    public RestAction<T> delay(long delay, TimeUnit unit) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        return of(
            supplier,
            result -> executor.schedule(() -> successHandler.accept(result), delay, unit),
            error -> executor.schedule(() -> failureHandler.accept(error), delay, unit)
        );
    }
}

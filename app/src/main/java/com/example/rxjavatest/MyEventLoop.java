package com.example.rxjavatest;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import io.netty.channel.*;
import io.netty.util.concurrent.*;
import io.netty.util.internal.ObjectUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class MyEventLoop implements EventLoop {
    @Override
    public EventLoopGroup parent() {
        return null;
    }

    @Override
    public boolean inEventLoop() {
        return false;
    }

    @Override
    public boolean inEventLoop(Thread thread) {
        return false;
    }

    @Override
    public <V> Promise<V> newPromise() {
        return null;
    }

    @Override
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return null;
    }

    @Override
    public <V> Future<V> newSucceededFuture(V result) {
        return null;
    }

    @Override
    public <V> Future<V> newFailedFuture(Throwable cause) {
        return null;
    }

    @Override
    public boolean isShuttingDown() {
        return false;
    }

    @Override
    public Future<?> shutdownGracefully() {
        return null;
    }

    @Override
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public Future<?> terminationFuture() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public EventLoop next() {
        return null;
    }

    @Override
    public Iterator<EventExecutor> iterator() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void forEach(@NonNull Consumer<? super EventExecutor> action) {
        EventLoop.super.forEach(action);
    }

    @NonNull
    @Override
    public Spliterator<EventExecutor> spliterator() {
        return EventLoop.super.spliterator();
    }

    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }

    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<java.util.concurrent.Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return null;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return null;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return null;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return null;
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return null;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return null;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return null;
    }

    @Override
    public ChannelFuture register(Channel channel) {
        return null;
    }

    @Override
    public ChannelFuture register(ChannelPromise promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        promise.channel().unsafe().register(this, promise);
        return promise;
    }

    @Override
    public ChannelFuture register(Channel channel, ChannelPromise promise) {
        return null;
    }

    @Override
    public void execute(Runnable command) {

    }
}

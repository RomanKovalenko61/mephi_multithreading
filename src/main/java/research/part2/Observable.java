package research.part2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Observable<T> {
    private final Consumer<Observer<T>> onSubscribe;

    private Observable(Consumer<Observer<T>> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public static <T> Observable<T> create(Consumer<Observer<T>> onSubscribe) {
        return new Observable<>(onSubscribe);
    }

    public Disposable subscribe(Observer<T> observer) {
        SafeObserver<T> safe = new SafeObserver<>(observer);
        onSubscribe.accept(safe);
        return safe;
    }

    public <R> Observable<R> flatMap(Function<? super T, Observable<R>> mapper) {
        return new Observable<>(emitter ->
                this.subscribe(new SafeObserver<>(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        mapper.apply(item).subscribe(new Observer<R>() {
                            @Override
                            public void onNext(R innerItem) {
                                emitter.onNext(innerItem);
                            }

                            @Override
                            public void onError(Throwable t) {
                                emitter.onError(t);
                            }

                            @Override
                            public void onComplete() {
                                // пусто
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                        emitter.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        emitter.onComplete();
                    }
                }))
        );
    }

    public <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return new Observable<>(downstream ->
                Observable.this.subscribe(new SafeObserver<>(new Observer<>() {
                    @Override
                    public void onNext(T item) {
                        downstream.onNext(mapper.apply(item));
                    }

                    @Override
                    public void onError(Throwable t) {
                        downstream.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        downstream.onComplete();
                    }
                }))
        );
    }

    public Observable<T> filter(Predicate<? super T> predicate) {
        return new Observable<>(downstream ->
                Observable.this.subscribe(new SafeObserver<>(new Observer<>() {
                    @Override
                    public void onNext(T item) {
                        if (predicate.test(item)) {
                            downstream.onNext(item);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        downstream.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        downstream.onComplete();
                    }
                }))
        );
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return new Observable<>(observer ->
                scheduler.execute(() -> Observable.this.onSubscribe.accept(observer))
        );
    }

    public Observable<T> observeOn(Scheduler scheduler) {
        return new Observable<>(downstream ->
                Observable.this.subscribe(new SafeObserver<>(new Observer<>() {
                    @Override
                    public void onNext(T item) {
                        scheduler.execute(() -> downstream.onNext(item));
                    }

                    @Override
                    public void onError(Throwable t) {
                        scheduler.execute(() -> downstream.onError(t));
                    }

                    @Override
                    public void onComplete() {
                        scheduler.execute(downstream::onComplete);
                    }
                }))
        );
    }

    private static class SafeObserver<T> implements Observer<T>, Disposable {
        private final Observer<T> actual;
        private final AtomicBoolean done = new AtomicBoolean(false);
        private final AtomicBoolean disposed = new AtomicBoolean(false);

        SafeObserver(Observer<T> actual) {
            this.actual = actual;
        }

        @Override
        public void onNext(T item) {
            if (!done.get() && !disposed.get()) actual.onNext(item);
        }

        @Override
        public void onError(Throwable t) {
            if (!done.get() && !disposed.get()) {
                done.set(true);
                actual.onError(t);
            }
        }

        @Override
        public void onComplete() {
            if (!done.get() && !disposed.get()) {
                done.set(true);
                actual.onComplete();
            }
        }

        @Override
        public void dispose() {
            disposed.set(true);
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }
}

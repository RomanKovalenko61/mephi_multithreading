package research.part2;

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

    public void subscribe(Observer<T> observer) {
        onSubscribe.accept(new SafeObserver<>(observer));
    }

    public <R> Observable<R> map(Function<? super T, ? extends R> mapper) {
        return new Observable<>(downstream ->
                Observable.this.subscribe(new Observer<T>() {
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
                })
        );
    }

    public Observable<T> filter(Predicate<? super T> predicate) {
        return new Observable<>(downstream ->
                Observable.this.subscribe(new Observer<T>() {
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
                })
        );
    }

    private static class SafeObserver<T> implements Observer<T> {
        private final Observer<T> actual;
        private boolean done = false;

        SafeObserver(Observer<T> actual) {
            this.actual = actual;
        }

        @Override
        public void onNext(T item) {
            if (!done) actual.onNext(item);
        }

        @Override
        public void onError(Throwable t) {
            if (!done) {
                done = true;
                actual.onError(t);
            }
        }

        @Override
        public void onComplete() {
            if (!done) {
                done = true;
                actual.onComplete();
            }
        }
    }
}

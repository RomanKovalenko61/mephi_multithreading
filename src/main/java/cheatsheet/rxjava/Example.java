package cheatsheet.rxjava;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class Example {
    public static void main(String[] args) {
        System.out.println("Example Observable");
        // just()
        Observable<Integer> observable = Observable.just(1, 2, 3);
        // fromIterable()
        List<String> list = Arrays.asList("A", "B", "C");
        Observable<String> observableEx1 = Observable.fromIterable(list);
        Observable<Integer> observableEx2 = Observable.create(emitter -> {
            try {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }
}

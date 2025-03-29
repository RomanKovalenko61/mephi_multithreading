package cheatsheet.rxjava;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        // Операторы преобразования (Transformation Operators)
        // map
        Observable.just(1, 2, 3, 4)
                .map(number -> number * 2) // Удваиваем каждый элемент
                .subscribe(result -> System.out.println("Result: " + result));
        // flatmap
        Observable.just("apple", "banana")
                .flatMap(fruit -> Observable.fromArray(fruit.split("")))
                .subscribe(letter -> System.out.println("Letter: " + letter));
        // scan
        Observable.just(1, 2, 3, 4)
                .scan((acc, number) -> acc + number) // Суммируем элементы поочередно
                .subscribe(result -> System.out.println("Accumulated Result: " + result));

        // Операторы фильтрации (Filtering Operators)
        // filter
        Observable.just(1, 2, 3, 4, 5, 6)
                .filter(number -> number % 2 == 0) // Оставляем только четные числа
                .subscribe(result -> System.out.println("Filtered Result: " + result));
        // take
        Observable.just(1, 2, 3, 4, 5)
                .take(3) // Берем только первые три элемента
                .subscribe(result -> System.out.println("Take Result: " + result));
        // distinct
        Observable.just(1, 2, 2, 3, 3, 3, 4)
                .distinct()
                .subscribe(result -> System.out.println("Distinct Result: " + result));

        // Операторы комбинирования (Combining Operators)
        // merge
        Observable<String> stream1 = Observable.just("A", "B");
        Observable<String> stream2 = Observable.just("1", "2");
        Observable.merge(stream1, stream2)
                .subscribe(result -> System.out.println("Merged Result: " + result));
        // zip
        Observable<String> names = Observable.just("John", "Jane");
        Observable<Integer> ages = Observable.just(30, 25);
        Observable.zip(names, ages, (name, age) -> name + " is " + age)
                .subscribe(result -> System.out.println("Zipped Result: " + result));

        // Операторы управления (Utility Operators)
        // delay
        // (?) вывод не соответствует учебнику!
        Observable.just("Delayed Message")
                .delay(2, TimeUnit.SECONDS)
                .subscribe(result -> System.out.println("Result: " + result));

        Observable.just("Message")
                .delay(3, TimeUnit.SECONDS)
                .timeout(2, TimeUnit.SECONDS)
                .subscribe(
                        result -> System.out.println("Result: " + result),
                        error -> System.out.println("Error: " + error)
                );
    }
}

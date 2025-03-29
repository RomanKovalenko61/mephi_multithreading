package cheatsheet.rxjava;

import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class SubjectEx {
    public static void main(String[] args) {
        System.out.println("PublishSubject: только новые элементы");
        PublishSubject<String> publishSubject = PublishSubject.create();
        publishSubject.onNext("Сообщение до подписки");
        publishSubject.subscribe(item -> System.out.println("Подписчик 1 получил: " + item));
        publishSubject.onNext("Сообщение 1");
        publishSubject.onNext("Сообщение 2");
        publishSubject.subscribe(item -> System.out.println("Подписчик 2 получил: " + item));
        publishSubject.onNext("Сообщение 3");

        System.out.println("BehaviorSubject: последнее значение + новые элементы");
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.createDefault("Начальное значение");
        behaviorSubject.subscribe(item -> System.out.println("Подписчик 1 получил: " + item));
        behaviorSubject.onNext("Состояние 1");
        behaviorSubject.onNext("Состояние 2");
        behaviorSubject.subscribe(item -> System.out.println("Подписчик 2 получил: " + item));
        behaviorSubject.onNext("Состояние 3");

        System.out.println("ReplaySubject: полный повтор истории");
        ReplaySubject<String> replaySubject = ReplaySubject.create();
        replaySubject.onNext("Событие 1");
        replaySubject.onNext("Событие 2");
        replaySubject.subscribe(item -> System.out.println("Подписчик 1 получил: " + item));
        replaySubject.onNext("Событие 3");
        replaySubject.subscribe(item -> System.out.println("Подписчик 2 получил: " + item));

        System.out.println("AsyncSubject: только последнее значение при завершении");
        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("Этап 1");
        asyncSubject.onNext("Этап 2");
        asyncSubject.subscribe(item -> System.out.println("Подписчик получил: " + item));
        asyncSubject.onNext("Этап 3");
        asyncSubject.onComplete();

    }
}

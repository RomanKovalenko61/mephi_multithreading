package cheatsheet.rxjava;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class SubjectEx2 {
    public static void main(String[] args) {
        /*
        Нужно, чтобы разные подписчики получали данные по-разному:

            - Один подписчик должен видеть только новые обновления.
            - Другой подписчик — последнее состояние и обновления.
            - Третий подписчик — всю историю обновлений с самого начала.
         */

        PublishSubject<String> publishSubject = PublishSubject.create();
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.createDefault("Начальное состояние");
        ReplaySubject<String> replaySubject = ReplaySubject.create();
// Подписчик 1 на PublishSubject
        publishSubject.subscribe(item -> System.out.println("PublishSubject подписчик получил: " + item));
// Подписчик 2 на BehaviorSubject
        behaviorSubject.subscribe(item -> System.out.println("BehaviorSubject подписчик получил: " + item));
// Подписчик 3 на ReplaySubject
        replaySubject.subscribe(item -> System.out.println("ReplaySubject подписчик получил: " + item));
// Эмитируем обновления состояния
        String[] updates = {"Обновление 1", "Обновление 2", "Обновление 3"};
        for (String update : updates) {
            publishSubject.onNext(update);
            behaviorSubject.onNext(update);
            replaySubject.onNext(update);
        }
    }
}

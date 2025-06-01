package research.part2;

public class Main {
    public static void main(String[] args) {
        System.out.println("RxJava Research Part 2");

        var integerObserver = new Observer<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("Получено: " + item);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Ошибка: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Завершено");
            }
        };

        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            //emitter.onError(new RuntimeException("Отправим ошибку!"));
            emitter.onNext(3);
            emitter.onComplete();
        });
        observable.subscribe(integerObserver);

        Observable<Integer> observable2 = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        });
        observable2.map(x -> x * 2)
                .filter(x -> x > 2)
                .subscribe(integerObserver);
    }
}
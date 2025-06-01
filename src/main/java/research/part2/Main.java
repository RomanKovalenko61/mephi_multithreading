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

        Scheduler io = new IOThreadScheduler();
        Scheduler single = new SingleThreadScheduler();
        Scheduler computation = new ComputationScheduler();

        Observer<Integer> threadsObserver = new Observer<>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("[Observer] onNext: " + item + " | Чтение в потоке: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("[Observer] onError: " + t.getMessage() + " | Чтение в потоке: " + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                System.out.println("[Observer] onComplete | Чтение в потоке: " + Thread.currentThread().getName());
            }
        };

        Observable<Integer> observable3 = Observable.create(emitter -> {
            System.out.println("[Observable] [Emitter: 100] Запуск в потоке: " + Thread.currentThread().getName());
            emitter.onNext(100);
            System.out.println("[Observable] [Emitter: 200] Запуск в потоке: " + Thread.currentThread().getName());
            emitter.onNext(200);
            System.out.println("[Observable] [Emitter: 300] Запуск в потоке: " + Thread.currentThread().getName());
            emitter.onNext(300);
            emitter.onComplete();
        });
        observable3
                .subscribeOn(single)
                .observeOn(io)
                //.observeOn(single)
                //.observeOn(computation)
                .map(x -> x + 1)
                .filter(x -> x > 100)
                .subscribe(threadsObserver);

        Observable<Integer> observable4 = Observable.create(emitter -> {
            System.out.println("[Observable] [Emitter: 100] Проверь запущен в потоке main?: " + Thread.currentThread().getName());
            emitter.onNext(100);
            System.out.println("[Observable] [Emitter: 200] Проверь запущен в потоке main?: " + Thread.currentThread().getName());
            emitter.onNext(200);
            System.out.println("[Observable] [Emitter: 300] Проверь запущен в потоке main?: " + Thread.currentThread().getName());
            emitter.onNext(300);
            emitter.onComplete();
        });
        observable4.map(x -> x + 1)
                .filter(x -> x > 100)
                .subscribe(threadsObserver);

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            System.out.println("Ошибка при ожидании: " + e.getMessage());
        }
    }
}
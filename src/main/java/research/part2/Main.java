package research.part2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
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
        var subscribe = observable.subscribe(integerObserver);
        System.out.println("Проверка отмены подписки" + subscribe.isDisposed());
        subscribe.dispose();
        System.out.println("Проверка после вызова метода dispose(): " + subscribe.isDisposed());

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
        //Scheduler computation = new ComputationScheduler();

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

        System.out.println("Запускаем в разных потоках...");
        Thread.sleep(1000);

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
                .map(x -> x + 10)
                .filter(x -> x > 100)
                .subscribe(threadsObserver);

        Thread.sleep(2000);
        System.out.println("Запускаем в потоке main...");

        Observable<Integer> observable4 = Observable.create(emitter -> {
            System.out.println("[Observable] [Emitter: 500] Проверь запущен в потоке main?: " + Thread.currentThread().getName());
            emitter.onNext(500);
            System.out.println("[Observable] [Emitter: 700] Проверь запущен в потоке main?: " + Thread.currentThread().getName());
            emitter.onNext(700);
            System.out.println("[Observable] [Emitter: 900] Проверь запущен в потоке main?: " + Thread.currentThread().getName());
            emitter.onNext(900);
            emitter.onComplete();
        });
        observable4.map(x -> x + 1)
                .filter(x -> x > 100)
                .subscribe(threadsObserver);

        Thread.sleep(2500);
        System.out.println("Тестируем flatMap...");

        Observable<Integer> flatMapSource = Observable.create(emitter -> {
            System.out.println("[FlatMap Source] Передаем число 5");
            emitter.onNext(5);
            System.out.println("[FlatMap Source] Передаем число 10");
            emitter.onNext(10);
            emitter.onComplete();
        });

        flatMapSource.flatMap(x -> {
            System.out.println("[FlatMap] Запускаем преобразование");
            return Observable.create(innerEmitter -> {
                innerEmitter.onNext(x * 2);
                innerEmitter.onNext(x * 10);
                innerEmitter.onComplete();
            });
        }).subscribe(new Observer<>() {
            @Override
            public void onNext(Object item) {
                System.out.println("[FlatMap Observer] Получено: " + item + " (Преобразованное число) ");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("[FlatMap Observer] Ошибка: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("[FlatMap Observer] Завершено");
            }
        });
    }
}
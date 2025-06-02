# Отчет по реализации собственной мини-RxJava

---

## Архитектура системы

Система построена вокруг паттерна Observer и реализует основные элементы реактивного программирования:

- **Observable\<T\>** — основной источник данных, который эмиттит элементы подписчикам.
- **Observer\<T\>** — интерфейс для получения событий (`onNext`, `onError`, `onComplete`).
- **Disposable** — интерфейс для управления жизненным циклом подписки.
- **SafeObserver** — обертка для Observer, предотвращающая повторные вызовы и ошибки.
- **Операторы**:  
  - `map` — преобразует элементы потока.
  - `filter` — фильтрует элементы по условию.
  - `flatMap` — разворачивает вложенные Observable.
  - `subscribeOn` — задает поток исполнения для подписки.
  - `observeOn` — задает поток исполнения для обработки событий.

Каждый оператор возвращает новый Observable, не изменяя исходный, что позволяет строить цепочки преобразований.

---

## Принципы работы Schedulers

**Schedulers** — абстракция для управления потоками исполнения:

- **IOThreadScheduler**  
  Использует пул потоков (`Executors.newCachedThreadPool()`).  
  Предназначен для операций ввода-вывода, сетевых запросов, работы с файлами.  
  Позволяет выполнять задачи параллельно, не блокируя основной поток.

- **SingleThreadScheduler**  
  Использует один поток (`Executors.newSingleThreadExecutor()`).  
  Подходит для последовательных задач, где важен порядок исполнения.

- **ComputationScheduler**  
  Использует пул потоков (`Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())`).  
  Предназначен для операций ввода-вывода, сетевых запросов, работы с файлами.  
  Позволяет выполнять задачи параллельно, при этом ограничивая количество потоков количеством доступных процессоров.

**Применение:**  
- `subscribeOn(scheduler)` — определяет, в каком потоке будет происходить подписка и генерация событий.
- `observeOn(scheduler)` — определяет, в каком потоке будут обрабатываться события у Observer.

Это позволяет гибко управлять многопоточностью и избегать блокировок главного потока.

---

## Процесс тестирования и основные сценарии

**Тестирование** проводится через написание сценариев в `Main.java`:

- **Проверка базовой подписки:**  
  Создание Observable, эмиттинг значений, обработка через Observer.
- **Тестирование операторов:**  
  - `map` и `filter` — проверка корректности преобразования и фильтрации.
  - `flatMap` — проверка разворачивания вложенных Observable и объединения потоков.
- **Потоковые сценарии:**  
  Использование разных Schedulers для генерации и обработки событий, проверка вывода имени потока.
- **Обработка ошибок:**  
  Проверка передачи ошибок через onError.
- **Завершение потока:**  
  Проверка вызова onComplete.

**Пример теста flatMap:**
```java
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
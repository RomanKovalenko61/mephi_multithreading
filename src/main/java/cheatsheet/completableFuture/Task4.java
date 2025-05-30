package cheatsheet.completableFuture;

import java.util.concurrent.CompletableFuture;

public class Task4 {
    public static void main(String[] args) {
        // Interface CompletionStage
        // Например, thenApply(), thenCompose() и другие методы CompletableFuture являются частью интерфейса CompletionStage
        // и позволяют строить последовательные цепочки задач, где результат одного этапа используется в следующем.
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Привет")
                .thenApply(greeting -> greeting + ", мир!")
                .thenApply(fullGreeting -> fullGreeting + " Как дела?");
        System.out.println(future.join()); // Привет, мир! Как дела?
    }
}

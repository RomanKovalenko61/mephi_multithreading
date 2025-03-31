package week5;

import io.reactivex.rxjava3.core.Single;

public class Task1 {
    public static void main(String[] args) {
        AsyncSumCalculator.calculateSum(5).subscribe(
                result -> System.out.println("Sum = " + result),
                error -> System.out.println("Error...")
        );
    }

    static class AsyncSumCalculator {
        public static Single<Integer> calculateSum(int n) {
            // Ваше решение
            return Single.create(emitter -> {
                int sum = 0;
                for (int i = 1; i <= n; i++) {
                    sum += i;
                }
                emitter.onSuccess(sum);
            });
        }
    }
}
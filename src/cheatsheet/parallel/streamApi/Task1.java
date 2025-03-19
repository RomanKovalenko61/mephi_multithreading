package cheatsheet.parallel.streamApi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task1 {
    public static void main(String[] args) {
        Stream<Integer> stream = Stream.iterate(1, n -> n + 1).limit(10);
        System.out.println(stream.collect(Collectors.toList()));

        System.out.println("Single thread");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
                .map(n -> n * n)
                .forEach(System.out::println);

        System.out.println("Multi thread");
        List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5);
        numbers2.parallelStream()
                .map(n -> n * n)
                .forEach(System.out::println);
        System.out.println("convert stream");
        List<Integer> numbers3 = Arrays.asList(1, 2, 3, 4, 5);
        numbers3.stream()
                .parallel() // Преобразуем в параллельный поток
                .map(n -> n * n)
                .forEach(System.out::println);
    }
}

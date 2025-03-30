package week4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task2 {
    public static void main(String[] args) {
        System.out.println("Тестируем метод foo() с сотрудниками...");

        List<Employee> employees = Arrays.asList(
                new Employee("John", "IT", 60000, 35),
                new Employee("Alice", "HR", 70000, 40),
                new Employee("Bob", "IT", 50000, 25),
                new Employee("Eve", "Finance", 80000, 45),
                new Employee("Charlie", "HR", 70000, 32),
                new Employee("Dave", "Finance", 55000, 38)
        );


        Map<String, List<Employee>> result = StreamApiTask.foo(employees);

        System.out.println("Результат:");
        printResult(result);
    }

    private static void printResult(Map<String, List<Employee>> result) {
        for (Map.Entry<String, List<Employee>> entry : result.entrySet()) {
            System.out.println("key " + entry.getKey() + " value " + entry.getValue());
        }
    }

    record Employee(String name, String department, double salary, int age) {

        @Override
        public String toString() {
            return name + " (" +
                    department + ", " +
                    "$" + salary + ", " +
                    age + " лет" +
                    ')';
        }
    }

    static class StreamApiTask {
        public static Map<String, List<Employee>> foo(List<Employee> employee) {
            // Ваше решение
            return employee.parallelStream()
                    .filter(p -> p.age() > 30 && p.salary() > 50_000)
                    .sorted(Comparator.comparing(Employee::salary).reversed().thenComparing(Employee::name))
                    .collect(Collectors.groupingBy(Employee::department, Collectors.toList()));
        }
    }
}

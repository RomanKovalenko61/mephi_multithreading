package cheatsheet.parallel.streamApi;

import java.util.Arrays;
import java.util.List;

public class Task2 {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", 30, "HR", 70000, Arrays.asList("Communication", "Recruitment")),
                new Employee("Bob", 45, "IT", 120000, Arrays.asList("Java", "Spring", "Hibernate")),
                new Employee("Charlie", 25, "IT", 90000, Arrays.asList("JavaScript", "React")),
                new Employee("Diana", 35, "Finance", 110000, Arrays.asList("Accounting", "Excel")),
                new Employee("Ethan", 50, "IT", 150000, Arrays.asList("Java", "Microservices", "Docker"))
                // Добавьте больше сотрудников по необходимости
        );
        List<String> highEarningITEmployees = employees.stream()
                // Фильтруем сотрудников из IT отдела
                .filter(e -> "IT".equals(e.getDepartment()))
                // Фильтруем сотрудников с зарплатой выше 100000
                .filter(e -> e.getSalary() > 100000)
                // Преобразуем сотрудников в их имена
                .map(Employee::getName)
                // Сортируем имена по алфавиту
                .sorted()
                // Ограничиваем результат первыми 2 именами
                .limit(2).toList();
        System.out.println("Высокооплачиваемые сотрудники IT отдела: " + highEarningITEmployees);
    }

    static class Employee {
        private final String name;
        private final int age;
        private final String department;
        private final double salary;
        private final List<String> skills;

        public Employee(String name, int age, String department, double salary, List<String> skills) {
            this.name = name;
            this.age = age;
            this.department = department;
            this.salary = salary;
            this.skills = skills;
        }

        public String getDepartment() {
            return department;
        }

        public double getSalary() {
            return salary;
        }

        public String getName() {
            return name;
        }
    }
}

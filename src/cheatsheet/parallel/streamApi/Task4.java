package cheatsheet.parallel.streamApi;

import java.util.*;
import java.util.stream.Collectors;

public class Task4 {
    static class Student {
        private final String name;
        private final int age;
        private final String major;
        private final double gpa;
        private final List<String> courses;

        public Student(String name, int age, String major, double gpa, List<String> courses) {
            this.name = name;
            this.age = age;
            this.major = major;
            this.gpa = gpa;
            this.courses = courses;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getMajor() {
            return major;
        }

        public double getGpa() {
            return gpa;
        }

        public List<String> getCourses() {
            return courses;
        }
    }

    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Anna", 20, "Computer Science", 3.8, Arrays.asList("Algorithms", "Data Structures")),
                new Student("Brian", 22, "Mathematics", 3.6, Arrays.asList("Calculus", "Algebra")),
                new Student("Clara", 19, "Computer Science", 3.9, Arrays.asList("Operating Systems", "Networks")),
                new Student("David", 21, "Physics", 3.5, Arrays.asList("Quantum Mechanics", "Thermodynamics")),
                new Student("Eva", 23, "Mathematics", 3.7, Arrays.asList("Statistics", "Discrete Math")),
                new Student("Frank", 20, "Computer Science", 3.4, Arrays.asList("Machine Learning", "AI")),
                new Student("Grace", 22, "Physics", 3.9, Arrays.asList("Astrophysics", "Particle Physics")),
                new Student("Hannah", 19, "Mathematics", 3.8, Arrays.asList("Topology", "Number Theory"))
                // Добавьте больше студентов по необходимости
        );
        Map<String, Double> averageGpaByMajor = students.stream()
                // Фильтруем студентов старше 20 лет
                .filter(s -> s.getAge() > 20)
                // Фильтруем студентов с GPA выше 3.5
                .filter(s -> s.getGpa() > 3.5)
                // Преобразуем поток студентов в поток их курсов
                .flatMap(s -> s.getCourses().stream())
                // Фильтруем только курсы, содержащие слово "Math" или "Physics"
                .filter(course -> course.contains("Math") || course.contains("Physics"))
                // Маппим курсы обратно к студентам
                .map(course -> {
                    // Находим студентов, которые изучают данный курс
                    return students.stream()
                            .filter(s -> s.getCourses().contains(course))
                            .findFirst()
                            .orElse(null);
                })
                // Убираем возможные null значения
                .filter(Objects::nonNull)
                // Группируем студентов по их специальности
                .collect(Collectors.groupingBy(
                        Student::getMajor,
                        // Вычисляем средний GPA для каждой специальности
                        Collectors.averagingDouble(Student::getGpa)
                ))
                // Преобразуем результат в поток записей
                .entrySet().stream()
                // Сортируем по среднему GPA в порядке убывания
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                // Ограничиваем результат первыми 3 специальностями
                .limit(3)
                // Собираем результат в LinkedHashMap для сохранения порядка
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        averageGpaByMajor.forEach((major, avgGpa) ->
                System.out.println("Специальность: " + major + ", Средний GPA: " + avgGpa)
        );
    }
}

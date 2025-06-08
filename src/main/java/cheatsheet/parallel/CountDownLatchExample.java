package cheatsheet.parallel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchExample {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(15); //максимальный набор в группу 15 человек
        for (int i = 1; i < 16; i++) {     // создадим 15 желающих
            new Thread(new Student(i, countDownLatch)).start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


class Student implements Runnable {
    private final CountDownLatch countDownLatch;
    private final int number;

    public Student(int number, CountDownLatch countDownLatch) {
        this.number = number;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        countDownLatch.countDown();
        System.out.println("Добавился новый студент под номером "+number+". Осталось набрать " + countDownLatch.getCount());
        try {
            // здесь студент приостанавливается и ждет, пока наберется группа больше 15 человек
            countDownLatch.await();

            // если прошло 5 секунд, но группа не набралась, то студент начинает свою учебу
//            var await = countDownLatch.await(5L, TimeUnit.SECONDS);
//            if (!await) {
//                System.out.println("Студент " + number +" уже заждался, но группа не набралась");
//            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Студент " + number + " начал занятия");
    }
}

package cheatsheet.threads;

public class Ex {
    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Количество доступных процессоров: " + cores);
    }
}

package cheatsheet.projectloom;

public class VirtualThreadsExample {
    public static void main(String[] args) {
        Thread.ofVirtual().start(() -> System.out.println("Running in a virtual thread"));
        Thread mainThread = Thread.currentThread();
        System.out.println(mainThread + " -> основной поток");
    }
}

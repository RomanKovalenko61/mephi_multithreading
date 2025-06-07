package cheatsheet.projectloom;

public class VirtualThreadsSimpleExample {
    public static void main(String[] args) throws InterruptedException {
        Thread.startVirtualThread(() -> System.out.println("Hello from a virtual thread!"));
        Thread.sleep(1000);
    }
}

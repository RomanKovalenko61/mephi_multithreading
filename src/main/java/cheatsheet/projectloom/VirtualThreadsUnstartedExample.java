package cheatsheet.projectloom;

public class VirtualThreadsUnstartedExample {
    public static void main(String[] args) throws InterruptedException {
        Thread unstarted = Thread.ofVirtual().unstarted(() -> System.out.println("Hello from an unstarted virtual thread!"));
        Thread.sleep(1000);
        System.out.println("Starting the unstarted virtual thread...");
        unstarted.start();
        Thread.sleep(1000);
    }
}

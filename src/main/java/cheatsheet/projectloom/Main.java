package cheatsheet.projectloom;

// works in my machine with 21 version --enable-preview
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.ofVirtual().start(() -> {
            System.out.println("Running in a virtual thread");
        });
        thread.join();
    }
}

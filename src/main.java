package src;

public class main{
    /**
     * This class serves only as a thread manager and shouldn't be used otherwise
     * @param args mal account credentials
     */
    public static void main(String[] args) {
        Thread thread = new Task(args);
        thread.start();
    }
}

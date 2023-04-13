package src;

import java.io.IOException;

public class main{
    /**
     * This class serves only as a thread manager and shouldn't be used otherwise
     * @param args mal account credentials
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread malThread = new MalApiTask(args[2]);
        malThread.start();

        Thread thread = new Task(new String[]{args[0], args[1]});
        thread.start();
        //TODO: get access token automatically using http server and selenium
    }
}

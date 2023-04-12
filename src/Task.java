package src;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Task extends Thread{
    private Client client;

    public Task(String[] args){
        client = new Client();
        client.log_in_mal(args);
        System.out.println(client.get_banner_src());
    }

    @Override
    public void run() {
        do {

            System.out.println("Starting execution");
            client.start_searching();
            WebElement response = client.wait_for_recommendation_calculation();
            response = response.findElement(By.cssSelector("figcaption"));
            System.out.println(response.getText());
            try {
                Thread.sleep(5*60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }while(true);
    }
}

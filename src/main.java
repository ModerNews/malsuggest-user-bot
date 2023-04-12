package src;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import src.Client;

public class main {
    public static void main(String[] args) {
        Client client = new Client();
        client.log_in_mal(args);
        System.out.println(client.get_banner_src());
        client.start_searching();
        WebElement response = client.wait_for_recommendation_calculation();
        response = response.findElement(By.cssSelector("figcaption"));
        System.out.println(response.getText());
        client.quit();
    }
}

package src;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class client {
    public static void main(String[] args){
        WebDriver driver = new FirefoxDriver();

        driver.get("https://malsuggest.live");

        WebElement element = driver.findElement(By.cssSelector("img.banner"));
        System.out.println(element.toString());
        driver.quit();
    }
}

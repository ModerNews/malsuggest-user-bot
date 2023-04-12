package src;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Client {

    /**
     * This is default constructor for Client object.
     * It will create new clean FirefoxDriver, and store mal credentials to log in with later
     */
    public Client() {
        this.driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    private WebDriver driver;

    public String log_in_mal(String[] credentials){
        driver.get("https://myanimelist.net/login.php?from=%2F");
        System.out.println("Logging in with credentials:\n" + Arrays.toString(credentials));
        // Accept Cookies before proceeding
        WebElement cookies_btn = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/button[3]"));
        cookies_btn.click();
        WebElement username = driver.findElement(By.cssSelector("#loginUserName"));
        username.sendKeys(credentials[0]);
        WebElement password = driver.findElement(By.cssSelector("#login-password"));
        password.sendKeys(credentials[1]);
        WebElement submit_btn = driver.findElement(By.cssSelector(".inputButton.btn-form-submit"));
        submit_btn.click();
        // Wait for redirect to the main page
        try {
            driver.wait();
        }
        catch (Exception e) {
            driver.get("https://myanimelist.net/login.php");
        }
        Cookie session_id = driver.manage().getCookieNamed("is_logged_in");
        return session_id.getValue();
    }

    public void start_searching(){
        driver.get("https://malsuggest.live");
        clear_cookies();
        driver.navigate().refresh();
        WebElement start_search = driver.findElement(By.cssSelector(".btn-primary"));
        start_search.click();
        Cookie is_logged_in = driver.manage().getCookieNamed("is_logged_in");
        if(!(is_logged_in.getValue().equals("1"))) {
            System.out.println("Log in cookie status: " + is_logged_in.getValue());
            return;
        }
        WebElement allow_button = driver.findElement(By.cssSelector(".button--primary"));
        allow_button.click();
    }

    public void clear_cookies(){
        driver.manage().deleteAllCookies();
    }

    public WebElement wait_for_recommendation_calculation() {
        return new WebDriverWait(driver, Duration.of(60, ChronoUnit.SECONDS)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#top-recommendation")));
    }

    public String get_banner_src(){
        driver.get("https://malsuggest.live");

        WebElement element = driver.findElement(By.cssSelector("img.banner"));
        return element.getAttribute("src");
    }

    public void quit(){
        driver.quit();
    }
}

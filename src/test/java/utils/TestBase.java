package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import pages.Dice;

public class TestBase {

    public WebDriver driver;
    public Dice dice;

    @BeforeMethod
    public void setUp() {
        //System.setProperty("webdriver.gecko.driver", "src\\test\\resources\\drivers\\geckodriver\\win64\\geckodriver.exe");
        //driver = new FirefoxDriver();
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\drivers\\chrome\\win32\\chromedriver.exe");
        driver = new ChromeDriver();
        dice = new Dice(driver);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown(){
        driver.close();
    }
}
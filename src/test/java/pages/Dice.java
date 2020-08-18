package pages;

import org.openqa.selenium.WebDriver;

public class Dice {

    public HomePage homePage;
    public ResultPage resultPage;

    public Dice(WebDriver driver) {
        homePage = new HomePage(driver);
        resultPage = new ResultPage(driver);
    }
}
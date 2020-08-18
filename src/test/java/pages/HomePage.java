package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.CommonSteps;
import utils.TestBase;

public class HomePage extends TestBase {

    private static final String MAIN_PAGE_URL = "https://www.dice.com/";
    private final By JobTitleElement = new By.ByXPath("//*[@id=\"searchInput-div\"]/form/div/div[1]/div/dhi-new-typeahead-input/div/input");
    private final By LocationElement = new By.ByXPath("//*[@id=\"google-location-search\"]");
    private final By SearchButtonElement = new By.ByXPath("//*[@id=\"submitSearch-button\"]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get(MAIN_PAGE_URL);
    }

    public void typeJobTile(String inputJobTitleData) {
        CommonSteps.waitForElement(driver, JobTitleElement);
        WebElement inputJobTitle = driver.findElement(JobTitleElement);
        inputJobTitle.clear();
        inputJobTitle.sendKeys(inputJobTitleData);
    }

    public void typeLocation(String inputLocationData) {
        CommonSteps.waitForElement(driver, LocationElement);
        WebElement inputLocation = driver.findElement(LocationElement);
        inputLocation.clear();
        inputLocation.sendKeys(inputLocationData);
    }

    public void submitSearch() {
        CommonSteps.waitForElement(driver, SearchButtonElement);
        driver.findElement(SearchButtonElement).click();
    }
}
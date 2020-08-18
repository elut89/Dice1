package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utils.CommonSteps;
import utils.TestBase;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ResultPage extends TestBase {

    private final By excludeRemoteOptionsElement = new By.ByXPath("//*[@id=\"facets\"]/dhi-accordion[1]/div[2]/div/js-remote-options-filter/div/ul[1]/li[2]/span");
    private final By postedDateTodayElement = new By.ByXPath("//*[@id=\"facets\"]/dhi-accordion[2]/div[2]/div/js-single-select-filter/div/div/button[2]");
    private final By employmentTypeFullTimeElement = new By.ByXPath("//*[@id=\"facets\"]/dhi-accordion[3]/div[2]/div/js-multi-select-filter/ul/li[1]/span");
    private final By pageSizeElement = new By.ByXPath("//*[@id=\"pageSize_2\"]");
    private final By totalJobCountElement = new By.ByXPath("//*[@id=\"totalJobCount\"]");
    private final By radiusValueElement = new By.ByXPath("//*[@id=\"facets\"]/dhi-accordion[4]/div[2]/div/dhi-radius-filter-widget/dhi-radius-filter/form/div[1]/div/input");
    private final By searchJobsButtonElement = new By.ByXPath("//*[@id=\"submitSearch-button\"]");
    private final By resultLocationElement = new By.ByXPath("/html/body/dhi-js-dice-client/div/dhi-search-page-container/dhi-search-page/div/dhi-search-page-results/div/div[3]/js-search-display/div/div[2]/dhi-search-cards-widget/div/dhi-search-card[1]/div/div[1]/div/div[2]/div[1]/div/span");
    private final By originLocationWebElement = new By.ByXPath("//*[@id=\"facets\"]/dhi-accordion[4]/div[2]/div/dhi-radius-filter-widget/dhi-radius-filter/form/div[1]/span");

    public String originLocation;
    public int inputRadiusValueInt;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
    }

    // set page parameters for viewing results
    public void setResultPageParameters() {
        CommonSteps.waitForElement(driver, excludeRemoteOptionsElement);
        checkExcludeRemoteOptions();
        CommonSteps.waitForElement(driver, postedDateTodayElement);
        chosePostedDateToday();
        CommonSteps.waitForElement(driver, employmentTypeFullTimeElement);
        choseEmploymentTypeFullTime();
        CommonSteps.waitForElement(driver, pageSizeElement);
        selectPageSize();
    }

    public void checkExcludeRemoteOptions() {
        WebElement excludeRemoteOptions = driver.findElement(excludeRemoteOptionsElement);
        excludeRemoteOptions.click();
    }

    public void chosePostedDateToday() {
        WebElement postedDateToday = driver.findElement(postedDateTodayElement);
        postedDateToday.click();
    }

    public void choseEmploymentTypeFullTime() {
        WebElement employmentTypeFullTime = driver.findElement(employmentTypeFullTimeElement);
        employmentTypeFullTime.click();
    }

    public void selectPageSize() {
        WebElement pageSize = driver.findElement(pageSizeElement);
        Select selectPageSize = new Select(pageSize);
        selectPageSize.selectByVisibleText("100");
    }

    // input Radius Value
    public void inputRadiusValue(String inputRadiusValue) {
        CommonSteps.waitForElement(driver, radiusValueElement);
        CommonSteps.waitForElement(driver, searchJobsButtonElement);

        WebElement inputRadius = driver.findElement(radiusValueElement);
        inputRadius.clear();
        inputRadius.sendKeys(inputRadiusValue);

        WebElement searchJobsButton = driver.findElement(searchJobsButtonElement);
        searchJobsButton.click();

        double inputRadiusValueDbl = Math.floor(Double.parseDouble(inputRadiusValue));
        inputRadiusValueInt = (int) inputRadiusValueDbl;
    }

    // counts how many Job Cards were found
    public int getTotalJobCount() {
        CommonSteps.waitForElement(driver, totalJobCountElement);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int totalJobCount = Integer.parseInt(driver.findElement(totalJobCountElement).getText());
        if(totalJobCount > 100) { totalJobCount = 100; } // max 100 Job Cards
        return totalJobCount;
    }

    // get Origin Location
    public String getOriginLocation() {
        CommonSteps.waitForElement(driver, originLocationWebElement);
        WebElement originLocationElement = driver.findElement(originLocationWebElement);
        this.originLocation = originLocationElement.getText().substring(12);  //substring for delete "radius from " = 12
        return this.originLocation;
    }

    // create Array with the index number of the Job Cards
    public int[] getIndexNumberOfJobCards() {
        int[] indexNumberOfJobCards = new int[getTotalJobCount()];
        for (int i = 0; i < indexNumberOfJobCards.length; i++) {
            indexNumberOfJobCards[i] = i + 1;
        }
        return indexNumberOfJobCards;
    }

    // create Array with the Destination Locations
    public ArrayList<String> getResultLocations() {
        CommonSteps.waitForElement(driver, resultLocationElement);
        ArrayList<String> resultLocations = new ArrayList<>();
        for (int i = 1; i < getTotalJobCount() + 1; i++) {
            // execute each card with job description and location and add to the Array
            String resultLocationWebElement = "/html/body/dhi-js-dice-client/div/dhi-search-page-container/dhi-search-page/div/dhi-search-page-results/div/div[3]/js-search-display/div/div[2]/dhi-search-cards-widget/div/dhi-search-card[" + i + "]/div/div[1]/div/div[2]/div[1]/div/span";
            WebElement selectResultLocation = driver.findElement(By.xpath(resultLocationWebElement));
            String selectResultLocationDirty = selectResultLocation.getText();
            String selectResultLocationClean;
            if (selectResultLocationDirty.contains("Remote")) {  //clean if string has " Remote or ..."
                selectResultLocationClean = selectResultLocationDirty.substring(10);
            } else {
                selectResultLocationClean = selectResultLocationDirty;
            }
            resultLocations.add(selectResultLocationClean);
        }
        return resultLocations;
    }

    // create Array with Distance Between Origin And Destination Locations from Google API
    public ArrayList<Integer> getDistanceBetweenOriginAndDestination() {
        ArrayList<Integer> resultDistance = new ArrayList<>();
        for(String getDestinationLocation : getResultLocations()) {
            resultDistance.add(CommonSteps.getDistanceBetweenOriginAndDestination(getOriginLocation(), getDestinationLocation));
            }
        return resultDistance;
    }

    // Get Ok or Error for job location radius
    public ArrayList<String> getDistanceCheckResults() {
        ArrayList<String> distanceCheckResults = new ArrayList<>();
        for(int temp : getDistanceBetweenOriginAndDestination())
            if(temp > inputRadiusValueInt){
                distanceCheckResults.add("Error. Job Location outside the search radius.");
            } else {
                distanceCheckResults.add("Ok. Job Location inside the search radius.");
            }
        return distanceCheckResults;
    }

    // create Array of Error for Job Location which is outside the search radius.
    public ArrayList<String> createJobLocationOutsideTheSearchRadiusArray() {
        ArrayList<String> jobLocationOutsideTheSearchRadiusArray = new ArrayList<>();
        for (int i = 0; i < getDistanceBetweenOriginAndDestination().size(); i++) {
            if(getDistanceCheckResults().get(i).contains("Error. Job Location outside the search radius.")) {
                jobLocationOutsideTheSearchRadiusArray.add(Array.get(getIndexNumberOfJobCards(), i) + ".|" + originLocation + "|" + getResultLocations().get(i) + "|" + getDistanceBetweenOriginAndDestination().get(i) + " mi.|" + getDistanceCheckResults().get(i));
            }
        }
        return jobLocationOutsideTheSearchRadiusArray;
    }




    public Double[] getCoordinatesForOrigin() {
        Double[] coordinatesOrigin;
        coordinatesOrigin = CommonSteps.getLatitudeLongitude(getOriginLocation());
        return coordinatesOrigin;
    }

    // create Array with Destination Latitude
    public ArrayList<Double> getLatitudeForDestination() {
        ArrayList<Double> latitudeDestination = new ArrayList<>();
        for(String destinationLocation :  getResultLocations()) {
            latitudeDestination.add(CommonSteps.getLatitudeLongitude(destinationLocation)[0]);
        }
        return latitudeDestination;
    }

    // create Array with Destination Latitude
    public ArrayList<Double> getLongitudeForDestination() {
        ArrayList<Double> longitudeDestination = new ArrayList<>();
        for(String destinationLocation :  getResultLocations()) {
            longitudeDestination.add(CommonSteps.getLatitudeLongitude(destinationLocation)[1]);
        }
        return longitudeDestination;
    }

    // create Array with Distance Between Origin And Destination Locations calculates in the CommonSteps class
    public ArrayList<Integer> getCalculatedDistanceBetweenOriginAndDestination() {
        ArrayList<Integer> resultDistance = new ArrayList<>();
        for(int i = 0; i < getResultLocations().size(); i++) {
            resultDistance.add(CommonSteps.getDistance(getCoordinatesForOrigin()[0], getCoordinatesForOrigin()[1], getLatitudeForDestination().get(i), getLongitudeForDestination().get(i)));
        }
        return resultDistance;
    }

    // Get Ok or Error for job location radius
    public ArrayList<String> getCalculatedDistanceCheckResults() {
        ArrayList<String> distanceCheckResults = new ArrayList<>();
        for(int temp : getCalculatedDistanceBetweenOriginAndDestination())
            if(temp > inputRadiusValueInt){
                distanceCheckResults.add("Error. Job Location outside the search radius.");
            } else {
                distanceCheckResults.add("Ok. Job Location inside the search radius.");
            }
        return distanceCheckResults;
    }

    // create Array of Error for Job Location which is outside the search radius calculated.
    public ArrayList<String> createJobLocationOutsideTheSearchRadiusArrayCalculated() {
        ArrayList<String> jobLocationOutsideTheSearchRadiusArray = new ArrayList<>();
        for (int i = 0; i < getCalculatedDistanceCheckResults().size(); i++) {
            if(getCalculatedDistanceCheckResults().get(i).contains("Error. Job Location outside the search radius.")) {
                jobLocationOutsideTheSearchRadiusArray.add(Array.get(getIndexNumberOfJobCards(), i) + ".|" + originLocation + "|" + getResultLocations().get(i) + "|" + getCalculatedDistanceBetweenOriginAndDestination().get(i) + " mi.|" + getCalculatedDistanceCheckResults().get(i));
            }
        }
        return jobLocationOutsideTheSearchRadiusArray;
    }

    // create a Error Log File. It stores all jobs that are outside the search radius.
    public void createErrorLogFile(String fileNameError) throws IOException {
        int bufferSize = 8 * 1024;
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameError + "ErrorLog_" + CommonSteps.getDateStr() + ".txt"), bufferSize);
        switch (fileNameError) {
            case "testJobLocationSearchRadius":
                for (String fileContent : createJobLocationOutsideTheSearchRadiusArray()) {
                    writer.write(fileContent + "\n");
                }
                writer.flush();
                writer.close();
                break;
            case "testJobLocationSearchRadiusCalculate":
                for (String fileContent : createJobLocationOutsideTheSearchRadiusArrayCalculated()) {
                    writer.write(fileContent + "\n");
                }
                writer.flush();
                writer.close();
                break;
        }
    }
}
package testScripts;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CommonSteps;
import utils.TestBase;
import java.io.IOException;


public class RadiusSearchTest extends TestBase {

    //Requirement:
        //"Result Job Location" must be within the search radius value.
        // Not to exceeds the search radius value.

    //1. open Dice HomePage
    //2. input Job Tile
    //3. input Location
    //4. submit
    //5. parameterize the ResultPage
    //6. input Radius Value
    //7. get all Job Locations from Job Cards (max = 100)
    //8. using the "Distance Matrix API" calculate the distance between origin location and destinations
    //9. compare distance from all destinations with the origin location
    //10. create Common Log file in format: Job Card Index Number | origin location | destination | distance between origin and destination | Result(Ok/Error)
    //11. create Error Log file in format: Job Card Index Number | origin location | destination | distance between origin and destination | Result(Ok/Error)
    //12. if any Job Card is found with a Job Location that exceeds the input radius value, then the test fails

    //TODO set your GOOGLE_API_KEY in the CommonSteps class
    @Test
    public void testJobLocationSearchRadius() throws IOException {
        dice.homePage.open();
        dice.homePage.typeJobTile("QA Tester");  // input Job Title
        dice.homePage.typeLocation("Los Angeles"); // input Location
        dice.homePage.submitSearch();
        dice.resultPage.setResultPageParameters();
        dice.resultPage.inputRadiusValue("10");  // input search radius
        Assert.assertTrue(dice.resultPage.getTotalJobCount() > 0, "No Jobs Found");
        //dice.resultPage.createCommonLogFile("testJobLocationSearchRadiusCommonLog");
        dice.resultPage.createErrorLogFile("testJobLocationSearchRadius"); // fileNameError must be the same as test name
        Assert.assertFalse(dice.resultPage.getDistanceCheckResults().contains("Error. Job Location outside the search radius."), "The testJobLocationSearchRadius found Job Locations that are outside the search radius. Please check the: Error Log file with the timestamp: " + CommonSteps.getDateStr() );
    }

    @Test
    public void testJobLocationSearchRadiusCalculate() throws IOException {
        dice.homePage.open();
        dice.homePage.typeJobTile("QA Tester");
        dice.homePage.typeLocation("Los Angeles");
        dice.homePage.submitSearch();
        dice.resultPage.setResultPageParameters();
        dice.resultPage.inputRadiusValue("10");
        Assert.assertTrue(dice.resultPage.getTotalJobCount() > 0, "No Jobs Found");
        dice.resultPage.createCommonLogFile("testJobLocationSearchRadiusCalculate");
        dice.resultPage.createErrorLogFile("testJobLocationSearchRadiusCalculate"); // fileNameError must be the same as test name
        Assert.assertFalse(dice.resultPage.getCalculatedDistanceCheckResults().contains("Error. Job Location outside the search radius."), "The testJobLocationSearchRadiusCalculate found Job Locations that are outside the search radius. Please check the: Error Log file with the timestamp: " + CommonSteps.getDateStr() );
    }
}
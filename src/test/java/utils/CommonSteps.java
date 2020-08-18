package utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonSteps extends TestBase{
    protected static final String GOOGLE_API_KEY = "AIzaSyBKLgQNg5yHR79_gYbPZZIVKzl8adfB4sE";

    public static WebElement waitForElement(WebDriver driver, By expectedElement) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        WebElement foundElement = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(expectedElement));
        return foundElement;
    }

    // using the Google Distance Matrix API, calculates the distance between Input Location (origin) and Result of Job Location (destination)
    public static Integer getDistanceBetweenOriginAndDestination(String origins, String destinations) {
        RestAssured.baseURI = "https://maps.googleapis.com";
        RestAssured.basePath = "/maps/api";
        Response response = RestAssured
                .given()
                .param("units", "imperial")
                .param("origins", origins)
                .param("destinations", destinations)
                .param("key", GOOGLE_API_KEY)
                .when()
                .get("/distancematrix/json");
        int statusCode = response.getStatusCode(); //get status code 200
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        JsonPath jsonPathValidator = response.jsonPath();
        String statusOK = (jsonPathValidator.getString("status")); //get status OK
        Assert.assertEquals(statusOK,"OK", "Status: OK returned");
        String distance = (jsonPathValidator.getString("rows[0].elements[0].distance.text")); //get distance
        String distanceOnlyNumbers = distance.substring(0, distance.indexOf(' ')); //get distance numbers only without " mi" in String
        String distanceOnlyNumbersComma = distanceOnlyNumbers.replaceAll(",", ""); //delete comma
        double distanceFloatNumbers = Math.floor(Double.parseDouble(distanceOnlyNumbersComma)); // rounding down (floor)
        Integer distanceNum = (int)distanceFloatNumbers;
        return distanceNum;
    }

    // using Google Geocoding API, returns Latitude Longitude
    public static Double[] getLatitudeLongitude(String place) {
        RestAssured.baseURI = "https://maps.googleapis.com";
        RestAssured.basePath = "/maps/api";
        Response response = RestAssured
                .given()
                .param("address", "Los Angeles, CA, USA")
                .param("key", GOOGLE_API_KEY)
                .when()
                .get("/geocode/json");
        int statusCode = response.getStatusCode(); //get status code 200
        Assert.assertEquals(statusCode, 200, "Correct status code returned");
        JsonPath jsonPathValidator = response.jsonPath();
        String statusOK = (jsonPathValidator.getString("status")); //get status OK
        Assert.assertEquals(statusOK,"OK", "Status: OK returned");
        Double latitude = (jsonPathValidator.getDouble("results[0].geometry.location.lat")); //get Latitude For Origin
        Double longitude = (jsonPathValidator.getDouble("results[0].geometry.location.lng")); //get Longitude For Origin

        Double[] coordinates = new Double[2];
        coordinates[0] = latitude;
        coordinates[1] = longitude;
        return coordinates;
    }

    //  returns distance between two places
    public static Integer getDistance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            Integer destination = Math.round((int)dist);
            return destination;
        }
    }

    public static String getDateStr() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String dateStr = (formatter.format(date));
        return dateStr;
    }
}
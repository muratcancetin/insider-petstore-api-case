package tests;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.PetHelper;


import java.util.Arrays;

import static utils.ConfigReader.getProperty;

public class PetTests {

    int petID = 0;
    String petName = "";
    Response response;
    public static RequestSpecification requestSpecification;
    static Faker faker;

    @BeforeSuite(groups = {"positive_case", "negative_case"})
    public static void setup() {
        faker = new Faker();
        System.out.println("BASE_URI: " + getProperty("BASE_URI"));
        requestSpecification = new RequestSpecBuilder()
                .addFilters(Arrays.asList(new RequestLoggingFilter(), new ResponseLoggingFilter()))
                .setBaseUri(getProperty("BASE_URI"))
                .build();
    }

    @Test(groups = "positive_case", priority = 1)
    public void createNewPet() {
        response = PetHelper.createPet(faker.number().numberBetween(100, 200), faker.animal().name(), "available");
        Assert.assertEquals(response.getStatusCode(), 200);
        petID = Integer.parseInt(response.jsonPath().getString("id"));
        petName = response.jsonPath().getString("name");
    }

    @Test(dependsOnMethods = "createNewPet", groups = "positive_case", priority = 2)
    public void getPetWithId() {
        response = PetHelper.getPetById(petID);
        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("name"), petName);
    }

    @Test(dependsOnMethods = "createNewPet", groups = "positive_case", priority = 3)
    public void updatePet() {
        response = PetHelper.updatePet(petID, petName, "available");
        response.then().statusCode(200);
        Assert.assertEquals(response.jsonPath().getString("name"), petName);
    }

    @Test(dependsOnMethods = "createNewPet", groups = "positive_case", priority = 4)
    public void deletePet() {
        response = PetHelper.deletePet(petID);
        response.then().statusCode(200);
    }

    @Test(groups = "negative_case", priority = 5)
    public void failedDeletePet() {
        response = PetHelper.deletePet(-1);
        Assert.assertEquals(response.getStatusCode(), 404);
    }

}

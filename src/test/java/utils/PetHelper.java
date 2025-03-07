package utils;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static tests.PetTests.requestSpecification;

import java.util.HashMap;
import java.util.Map;


public class PetHelper {


    public static Response createPet(int petId, String petName, String petStatus) {
        try {
            Map<String, Object> petData = new HashMap<>();
            petData.put("id", petId);
            petData.put("name", petName);
            petData.put("status", petStatus);

            return given()
                    .spec(requestSpecification)
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body(petData)
                    .when()
                    .post("/pet");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Response getPetById(int petId) {
        try {
            return given()
                    .spec(requestSpecification)
                    .header("accept", "application/json")
                    .when()
                    .get("/pet/" + petId);
        } catch (Exception e) {
            System.err.println("Failed Get Pet By ID : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Response updatePet(int petId, String newName, String newStatus) {
        try {
            Map<String, Object> petData = new HashMap<>();
            petData.put("id", petId);
            petData.put("name", newName);
            petData.put("status", newStatus);

            return given()
                    .spec(requestSpecification)
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body(petData)
                    .when()
                    .put("/pet");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Response deletePet(int petId) {
        try {
            return given()
                    .spec(requestSpecification)
                    .header("accept", "application/json")
                    .when()
                    .delete("/pet/" + petId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

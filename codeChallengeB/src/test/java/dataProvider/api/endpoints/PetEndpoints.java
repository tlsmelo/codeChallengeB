package dataProvider.api.endpoints;

import dataProvider.api.TestAPI;
import dataProvider.api.datamodels.*;
import io.restassured.response.Response;
import utils.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PetEndpoints {

    final TestAPI testAPI;

    public PetEndpoints() {
        testAPI = new TestAPI();
    }

    String getBasePath() {
        return "/pet";
    }

    public Pet getPetById(int id) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .get("/"+id)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Pet.class);
    }

    public List<Pet> getPetByStatus(String status) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .queryParam("status", status)
                .get("/findByStatus")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return Arrays.asList(response.as(Pet[].class));
    }

    public List<Pet> getPetByTag(String tagName) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .queryParam("tags", tagName)
                .get("/findByTags")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return Arrays.asList(response.as(Pet[].class));
    }

    public Pet postPet(Pet pet) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .body(pet)
                .post()
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Pet.class);
    }

    public Pet postPetToUpdate(int id, String name, String status) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .queryParam("name", name)
                .queryParam("status", status)
                .post("/"+id)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Pet.class);
    }

    public int postPetImage(int id, File imageFile) {
        Response response = testAPI.getAPI()
                .contentType("application/octet-stream")
                //.header("Content-Type","application/octet-stream")
                //.accept("application/json")
                .basePath(getBasePath())
                .log().all()
                .body(imageFile)
                .post("/"+id+"/uploadImage")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.getStatusCode();
    }

    public Pet putPet(Pet pet) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .body(pet)
                .put()
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Pet.class);
    }

    public static Pet addPet() {
        PetEndpoints petEndpoints = new PetEndpoints();
        Category category = Category.builder().build();
        List<Tags> tags = Arrays.asList(Tags.builder().build());
        Pet pet = Pet.builder().category(category).tags(tags).build();
        return petEndpoints.postPet(pet);
    }

    public static int uploadImage(int id) {
        PetEndpoints petEndpoints = new PetEndpoints();
        String imageFilePath = FileUtils.makeSupportFilePath("dog.jpg");
        File file = new File(imageFilePath);
        return petEndpoints.postPetImage(id, file);
    }

    public static Pet addPetWithStatus(String status) {
        PetEndpoints petEndpoints = new PetEndpoints();
        Category category = Category.builder().build();
        List<Tags> tags = Arrays.asList(Tags.builder().build());
        Pet pet = Pet.builder().category(category).tags(tags).status(status).build();
        return petEndpoints.postPet(pet);
    }


    public static Pet addPetWithTag(String tagName) {
        PetEndpoints petEndpoints = new PetEndpoints();
        Category category = Category.builder().build();
        List<Tags> tags = Arrays.asList(Tags.builder().name(tagName).build());
        Pet pet = Pet.builder().category(category).tags(tags).build();
        return petEndpoints.postPet(pet);
    }

    public static Pet updatePet(int id) {
        PetEndpoints petEndpoints = new PetEndpoints();
        Category category = Category.builder().id(5).name("TestUp").build();
        List<Tags> tags = Arrays.asList(Tags.builder().id(2).name("TestPu").build());
        Pet pet = Pet.builder().id(id).status("pending").name("Luna").photoUrls(Arrays.asList("url3", "url4")).category(category).tags(tags).build();
        return petEndpoints.putPet(pet);
    }

    public static Pet updatePetByPost(int id, String name, String status) {
        PetEndpoints petEndpoints = new PetEndpoints();
        return petEndpoints.postPetToUpdate(id, name, status);
    }

    public int deletePet(int id) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .delete("/"+id)
                .then()
                .extract()
                .response();
        return response.getStatusCode();
    }

}

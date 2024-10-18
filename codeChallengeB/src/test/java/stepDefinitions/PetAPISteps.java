package stepDefinitions;

import dataProvider.api.datamodels.Pet;
import dataProvider.api.endpoints.PetEndpoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PetAPISteps {

    private final PetEndpoints petEndpoints;

    public PetAPISteps() {
        petEndpoints = new PetEndpoints();
    }

    @Given("I perform POST operation for pet")
    public void iPerformPostOperationForPet() {
        petEndpoints.addPet();
    }

    @Given("I perform POST operation for pet with status \"{}\"")
    public void iPerformPostOperationForPetWithStatus(String status) {
        petEndpoints.addPetWithStatus(status);
    }

    @Given("I perform POST operation for pet with tag \"{}\"")
    public void iPerformPostOperationForPetWitTag(String tagName) {
        petEndpoints.addPetWithTag(tagName);
    }

    @Given("I perform POST operation uploading an image for pet \"{}\"")
    public void iPerformPostOperationForPetWithStatus(int id) {
        int responseCode = petEndpoints.uploadImage(id);
        assertThat(responseCode, equalTo(200));
    }

    @Given("I perform PUT operation for pet \"{}\" update and check")
    public void iPerformPutOperationForPetUpdateAndCheck(int id) {
        Pet petInfo = petEndpoints.updatePet(id);
        assertThat(petInfo.getName(), equalTo("Luna"));
        assertThat(petInfo.getStatus(), equalTo("pending"));
        assertThat(petInfo.getCategory().getId(), equalTo(5));
        assertThat(petInfo.getCategory().getName(), equalTo("TestUp"));
        assertThat(petInfo.getPhotoUrls().get(0), equalTo("url3"));
        assertThat(petInfo.getPhotoUrls().get(1), equalTo("url4"));
        assertThat(petInfo.getTags().get(0).getName(), equalTo("TestPu"));
        assertThat(petInfo.getTags().get(0).getId(), equalTo(2));

        petEndpoints.deletePet(id);

    }

    @Given("I perform POST operation for pet \"{}\" update and check")
    public void iPerformPostOperationForPetUpdateAndCheck(int id) {
        Pet petInfo = petEndpoints.updatePetByPost(id, "Toto", "sold");
        assertThat(petInfo.getName(), equalTo("Toto"));
        assertThat(petInfo.getStatus(), equalTo("sold"));
    }

    @Given("I perform GET operation for pet by status \"{}\" and check")
    public void iPerformGetOperationForPetByStatusAndCheck(String status) {
        List<Pet> petInfo = petEndpoints.getPetByStatus(status);
        assertThat(petInfo.get(2).getId(), equalTo(11));
        assertThat(petInfo.get(2).getName(), equalTo("Rex"));
        assertThat(petInfo.get(2).getPhotoUrls().get(0), equalTo("url1"));
        assertThat(petInfo.get(2).getPhotoUrls().get(1), equalTo("url2"));
        assertThat(petInfo.get(2).getStatus(), equalTo(status));
        assertThat(petInfo.get(2).getCategory().getId(), equalTo(1));
        assertThat(petInfo.get(2).getCategory().getName(), equalTo("Dogs"));
        assertThat(petInfo.get(2).getTags().get(0).getId(), equalTo(1));
        assertThat(petInfo.get(2).getTags().get(0).getName(), equalTo("tag1"));

        petEndpoints.deletePet(11);
    }

    @Given("I perform GET operation for pet by tag \"{}\" and check")
    public void iPerformGetOperationForPetByTagAndCheck(String tagName) {
        List<Pet> petInfo = petEndpoints.getPetByTag(tagName);
        assertThat(petInfo.get(0).getId(), equalTo(11));
        assertThat(petInfo.get(0).getName(), equalTo("Rex"));
        assertThat(petInfo.get(0).getPhotoUrls().get(0), equalTo("url1"));
        assertThat(petInfo.get(0).getPhotoUrls().get(1), equalTo("url2"));
        assertThat(petInfo.get(0).getStatus(), equalTo("available"));
        assertThat(petInfo.get(0).getCategory().getId(), equalTo(1));
        assertThat(petInfo.get(0).getCategory().getName(), equalTo("Dogs"));
        assertThat(petInfo.get(0).getTags().get(0).getId(), equalTo(1));
        assertThat(petInfo.get(0).getTags().get(0).getName(), equalTo(tagName));

        petEndpoints.deletePet(11);
    }
    @And("I should see the pet by id \"{}\"")
    public void iShouldSeeThePetById(int id) {
        Pet petInfo = petEndpoints.getPetById(id);

        assertThat(petInfo.getId(), equalTo(11));
        assertThat(petInfo.getName(), equalTo("Rex"));
        assertThat(petInfo.getPhotoUrls().get(0), equalTo("url1"));
        assertThat(petInfo.getPhotoUrls().get(1), equalTo("url2"));
        assertThat(petInfo.getStatus(), equalTo("available"));
        assertThat(petInfo.getCategory().getId(), equalTo(1));
        assertThat(petInfo.getCategory().getName(), equalTo("Dogs"));
        assertThat(petInfo.getTags().get(0).getId(), equalTo(1));
        assertThat(petInfo.getTags().get(0).getName(), equalTo("tag1"));
    }

    @Then("I should see \"{}\" on response code for Pet")
    public void iShouldSeeResponseCodeForPet(int responseCode) {
        assertThat(response.getStatusCode(), equalTo(responseCode));
    }

}

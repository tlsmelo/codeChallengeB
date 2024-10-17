package stepDefinitions;

import dataProvider.ConfigFileReader;
import dataProvider.api.datamodels.User;
import dataProvider.api.endpoints.UserEndpoints;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserAPISteps {

    private final UserEndpoints userEndpoints;

    private static ResponseOptions<Response> response;

    public UserAPISteps() {
        userEndpoints = new UserEndpoints();
    }

    @Given("I perform GET operation for user \"{}\"")
    public void iPerformGetOperationForUser(String url) {
        if(url.equalsIgnoreCase("/login")) {
            response = userEndpoints.getUserLogin(ConfigFileReader.getInstance().getTestAPIUser(), ConfigFileReader.getInstance().getTestAPIPassword(), url);
        } else if (url.equalsIgnoreCase("/logout")) {
            response = userEndpoints.getUserLogout(url);
        }
    }

    @Given("I perform POST operation for user creation username \"{}\"")
    public void iPerformPostOperationForUserCreationUsername(String username) {
        userEndpoints.createUser(username);
    }

    @Given("I perform POST operation for users list creation usernames \"{}\"")
    public void iPerformPostOperationForUsersListCreationUsername(String usernames) {
        String[] usernamesSplit = usernames.split("[,\\;]");
        userEndpoints.createUserWithList(usernamesSplit);
    }

    @Given("I perform PUT operation for user update username \"{}\"")
    public void iPerformPutOperationForUserUpdateUsername(String username) {
        User userInfo = userEndpoints.updateUser(username);
        assertThat(userInfo.getPhone(), equalTo("55123458981"));
        assertThat(userInfo.getEmail(), equalTo("testupd@test.com"));
        assertThat(userInfo.getLastName(), equalTo("WZY987"));
        assertThat(userInfo.getFirstName(), equalTo("TestUpd"));
        assertThat(userInfo.getPassword(), equalTo("9876"));
    }

    @Given("I perform DELETE operation for user deletion username \"{}\"")
    public void iPerformDeleteOperationForUserDeletionUsername(String username) {
        int responseCode = userEndpoints.deleteUser(username);

        assertThat(responseCode, equalTo(200));
    }

    @Then("I should see \"{}\" on response code for User")
    public void iShouldSeeResponseCodeForUser(int responseCode) {
        assertThat(response.getStatusCode(), equalTo(responseCode));
    }

    @And("I should see the user session on response")
    public void iShouldSeeTheUserSessionInTheResponse() {
        assertThat(response.getBody().prettyPrint(), containsString("Logged in user session:"));

        System.out.println("User login session number is: " + response.getBody().prettyPrint().substring(24));
    }

    @And("I should see the logout message")
    public void iShouldSeeTheLogoutMessage() {
        assertThat(response.getBody().prettyPrint(), containsString("User logged out"));
    }

    @And("I should see the username \"{}\" created")
    public void iShouldSeeTheUsernameCreated(String username) {
        User userInfo = userEndpoints.getUserByUsername(username);
        assertThat(userInfo.getUsername(), equalTo(username));
        assertThat(userInfo.getUserStatus(), equalTo(1));
        assertThat(userInfo.getId(), equalTo(1));
        assertThat(userInfo.getFirstName(), equalTo("Test"));
        assertThat(userInfo.getLastName(), equalTo("ABC123"));
        assertThat(userInfo.getEmail(), equalTo("test@test.com"));
        assertThat(userInfo.getPassword(), equalTo("1234"));
        assertThat(userInfo.getPhone(), equalTo("551312555679"));
    }

    @And("I should see the username \"{}\" updated")
    public void iShouldSeeTheUsernameUpdated(String username) {
        User userInfo = userEndpoints.getUserByUsername(username);
        assertThat(userInfo.getPhone(), equalTo("55123458981"));
        assertThat(userInfo.getEmail(), equalTo("testupd@test.com"));
        assertThat(userInfo.getLastName(), equalTo("WZY987"));
        assertThat(userInfo.getFirstName(), equalTo("TestUpd"));
        assertThat(userInfo.getPassword(), equalTo("9876"));
    }

    @And("I should see the list of users \"{}\" created")
    public void iShouldSeeTheListOfUsersCreated(String usernames) {
        String[] usernamesSplit = usernames.split("[,\\;]");
        List<String> usernamesList = new ArrayList<>(Arrays.asList(usernamesSplit));
        User userInfo;

        for (int i=1; i<=usernamesList.size(); i++) {
            userInfo = userEndpoints.getUserByUsername(usernamesList.get(i-1));
            assertThat(userInfo.getUsername(), equalTo(usernamesList.get(i-1)));
            assertThat(userInfo.getUserStatus(), equalTo(1));
            assertThat(userInfo.getId(), equalTo(i));
            assertThat(userInfo.getFirstName(), equalTo("Test"));
            assertThat(userInfo.getLastName(), equalTo("ABC123"));
            assertThat(userInfo.getEmail(), equalTo("test@test.com"));
            assertThat(userInfo.getPassword(), equalTo("1234"));
            assertThat(userInfo.getPhone(), equalTo("551312555679"));
        }
    }

    @And("I should see the username \"{}\" deleted")
    public void iShouldSeeTheUsernameDeleted(String username) {
        int responseCode = userEndpoints.getUserByUsernameResponseCode(username);

        assertThat(responseCode, equalTo(404));
    }

}

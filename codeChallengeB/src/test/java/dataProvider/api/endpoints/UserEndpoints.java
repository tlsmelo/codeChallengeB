package dataProvider.api.endpoints;

import dataProvider.api.TestAPI;
import dataProvider.api.datamodels.User;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

public class UserEndpoints {

    final TestAPI testAPI;

    public UserEndpoints() {
        testAPI = new TestAPI();
    }

    String getBasePath() {
        return "/user";
    }

    public Response getUserLogin(String user, String password, String url) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .queryParam("username", user)
                .queryParam("password", password)
                .log().all()
                .get(url)
                .then()
                .extract()
                .response();
        return response;
    }

    public Response getUserLogout(String url) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .get(url)
                .then()
                .extract()
                .response();
        return response;
    }

    public User getUserByUsername(String username) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .get("/".concat(username))
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(User.class);
    }

    public int getUserByUsernameResponseCode(String username) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .get("/".concat(username))
                .then()
                .extract()
                .response();
        return response.getStatusCode();
    }

    public User postUser(User user) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .body(user)
                .post()
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(User.class);
    }

    public List<User> postUserWithList(List<User> users) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .body(users)
                .post("/createWithList")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(List.class);
    }

    public User updateUser(User user, String username) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .body(user)
                .put("/".concat(username))
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(User.class);
    }

    public int deleteUser(String username) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .delete("/".concat(username))
                .then()
                .extract()
                .response();
        return response.getStatusCode();
    }

    public static User createUser(String username) {
        UserEndpoints userEndpoints = new UserEndpoints();
        User user = User.builder().username(username).build();
        return userEndpoints.postUser(user);
    }

    public static List<User> createUserWithList(String[] usernames) {
        UserEndpoints userEndpoints = new UserEndpoints();
        List<User> users = new ArrayList<>();
        for (int i=1; i<=usernames.length; i++) {
            users.add(User.builder().id(i).username(usernames[i-1]).build());
        }
        return userEndpoints.postUserWithList(users);
    }

    public static User updateUser(String username) {
        UserEndpoints userEndpoints = new UserEndpoints();
        User user = User.builder().username(username).phone("55123458981").email("testupd@test.com").lastName("WZY987").firstName("TestUpd").password("9876").build();
        return userEndpoints.updateUser(user, username);
    }

}

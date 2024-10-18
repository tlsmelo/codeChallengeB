package dataProvider.api.endpoints;

import dataProvider.api.TestAPI;
import dataProvider.api.datamodels.Inventory;
import dataProvider.api.datamodels.Order;
import io.restassured.response.Response;

public class StoreEndpoints {

    final TestAPI testAPI;

    public StoreEndpoints() {
        testAPI = new TestAPI();
    }

    String getBasePath() {
        return "/store";
    }

    public Order getOrderById(int orderId) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .get("/order/"+orderId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Order.class);
    }

    public Response getOrderByIdResponse(int orderId) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .get("/order/"+orderId)
                .then()
                .extract()
                .response();
        return response;
    }

    public Inventory getInventory() {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .get("/inventory")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Inventory.class);
    }

    public Order postOrder(Order order, String url) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .body(order)
                .post(url)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract()
                .response();
        return response.as(Order.class);
    }

    public int deleteOrder(int orderId) {
        Response response = testAPI.getAPI()
                .basePath(getBasePath())
                .log().all()
                .delete("/order/"+orderId)
                .then()
                .extract()
                .response();
        return response.getStatusCode();
    }

    public static Order createOrder(String url) {
        StoreEndpoints storeEndpoints = new StoreEndpoints();
        Order order = Order.builder().quantity(1).shipDate("2024-10-30T20:21:02.711Z").status("approved").build();
        return storeEndpoints.postOrder(order, url);
    }

    public static Order createOrderWithValues(String url, int id, String status, int quantity) {
        StoreEndpoints storeEndpoints = new StoreEndpoints();
        Order order = Order.builder().id(id).quantity(quantity).shipDate("2024-10-30T20:21:02.711Z").status(status).build();
        return storeEndpoints.postOrder(order, url);
    }

}

package stepDefinitions;

import dataProvider.ConfigFileReader;
import dataProvider.api.datamodels.Inventory;
import dataProvider.api.datamodels.Order;
import dataProvider.api.datamodels.User;
import dataProvider.api.endpoints.StoreEndpoints;
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

public class StoreAPISteps {

    private final StoreEndpoints storeEndpoints;

    private static ResponseOptions<Response> response;

    public StoreAPISteps() {
        storeEndpoints = new StoreEndpoints();
    }

    @Given("I perform POST operation for store \"{}\"")
    public void iPerformPostOperationForStore(String url) {
        storeEndpoints.createOrder(url);
    }
    @And("I should see the purchase by order id \"{}\"")
    public void iShouldSeeThePurchaseByOrderId(int orderId) {
        Order orderInfo = storeEndpoints.getOrderById(orderId);
        assertThat(orderInfo.getId(), equalTo(1));
        assertThat(orderInfo.getPetId(), equalTo(1000));
        assertThat(orderInfo.getQuantity(), equalTo(1));
        assertThat(orderInfo.getStatus(), equalTo("approved"));
    }

    @Given("I perform GET operation for store inventory and POST operations for store \"{}\" and check inventory is updated")
    public void iPerformGetOperationForStoreInventoryAndPostOperationsForStoreAndCheckInventoryUpdated(String url) {
        Inventory inventoryInfo = storeEndpoints.getInventory();
        int qtyApproved = inventoryInfo.getApproved();
        int qtyPlaced = inventoryInfo.getPlaced();
        int qtyDelivered = inventoryInfo.getDelivered();

        storeEndpoints.createOrderWithValues(url, 21, "approved", 20);
        storeEndpoints.createOrderWithValues(url, 22, "placed", 10);
        storeEndpoints.createOrderWithValues(url, 23, "delivered", 5);

        inventoryInfo = storeEndpoints.getInventory();
        assertThat(inventoryInfo.getApproved(), equalTo(qtyApproved+20));
        assertThat(inventoryInfo.getPlaced(), equalTo(qtyPlaced+10));
        assertThat(inventoryInfo.getDelivered(), equalTo(qtyDelivered+5));

        //need to delete to back to the last status of inventory
        storeEndpoints.deleteOrder(21);
        storeEndpoints.deleteOrder(22);
        storeEndpoints.deleteOrder(23);

        inventoryInfo = storeEndpoints.getInventory();
        assertThat(inventoryInfo.getApproved(), equalTo(qtyApproved));
        assertThat(inventoryInfo.getPlaced(), equalTo(qtyPlaced));
        assertThat(inventoryInfo.getDelivered(), equalTo(qtyDelivered));

    }

    @Given("I perform GET operation for store for an inexistent order")
    public void iPerformGetOperationForStoreForAnInexistentOrder() {
        response = storeEndpoints.getOrderByIdResponse(50);
    }

    @Then("I should see \"{}\" on response code for Store")
    public void iShouldSeeResponseCodeForStore(int responseCode) {
        assertThat(response.getStatusCode(), equalTo(responseCode));
    }

}

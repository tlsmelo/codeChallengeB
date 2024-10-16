/*
 * Copyright (C) 2018-2020 TAG QA TEAM
 * This file is part of  TAG-automation
 * Created at 5/10/19 10:00 AM by llingerfelt
 */

package dataProvider.api;

import dataProvider.ConfigFileReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;

public class TestAPI {

    public TestAPI() {

    }

    public RequestSpecification getAPI() {

        return RestAssured.given(new RequestSpecBuilder().setBaseUri(ConfigFileReader.getInstance().getTestAPIUrl()).build())
                .header(new Header("Accept-Language", "en-US"))
                .contentType(ContentType.JSON);
    }
}
package com.revolut.demo.controller;

import com.revolut.demo.App;
import com.revolut.demo.config.DatasourceConfig;
import com.revolut.demo.constant.ApiConstants;
import com.revolut.demo.constant.RevolutResponseCode;
import com.revolut.demo.dto.TransferDto;
import io.javalin.Javalin;
import io.restassured.http.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.flywaydb.core.Flyway;
import org.jooq.tools.jdbc.SingleConnectionDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(JUnit4.class)
public class AccountControllerIntegrationTest {

    private static Javalin start;
    private static Connection connection;

    @BeforeClass
    public static void beforeClass() {
        connection = DatasourceConfig.getConnection();

        // This flyway db.migration should produce the same database as the DDLDatabase
        // used by the code generator, as it is configured using flyway file sorting
        Flyway flyway = Flyway.configure().dataSource(new SingleConnectionDataSource(AccountControllerIntegrationTest.connection)).load();
        flyway.migrate();

        start = App.getStart();
    }

    @Test
    public void should_transfer_money() {

        TransferDto transferDto = new TransferDto(1000, 2000, BigDecimal.valueOf(30), 1);

        given().body(transferDto)
                .when().post("http://localhost:" + ApiConstants.APPLICATION_PORT + ApiConstants.API_URL + "/transfer")
                .then().statusCode(HttpStatus.OK_200);
    }

    @Test
    public void should_not_transfer_money_for_unauthorized_user() {

        TransferDto transferDto = new TransferDto(2000, 1000, BigDecimal.valueOf(30), 1);

        given().body(transferDto)
                .when().post("http://localhost:" + ApiConstants.APPLICATION_PORT + ApiConstants.API_URL + "/transfer")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("success", equalTo(false))
                .body("error.code", equalTo(RevolutResponseCode.BR001.name()))
                .body("error.description", equalTo(RevolutResponseCode.BR001.getValue()));
    }

    @Test
    public void should_not_transfer_money_for_illegal_transaction() {

        TransferDto transferDto = new TransferDto(1000, 1000, BigDecimal.valueOf(30), 1);

        given().body(transferDto)
                .when().post("http://localhost:" + ApiConstants.APPLICATION_PORT + ApiConstants.API_URL + "/transfer")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("success", equalTo(false))
                .body("error.code", equalTo(RevolutResponseCode.BR002.name()))
                .body("error.description", equalTo(RevolutResponseCode.BR002.getValue()));
    }

    @Test
    public void should_not_transfer_money_for_non_active_sender_account() {

        TransferDto transferDto = new TransferDto(3000, 1000, BigDecimal.valueOf(30), 3);

        given().body(transferDto)
                .when().post("http://localhost:" + ApiConstants.APPLICATION_PORT + ApiConstants.API_URL + "/transfer")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("success", equalTo(false))
                .body("error.code", equalTo(RevolutResponseCode.BR003.name()))
                .body("error.description", equalTo(RevolutResponseCode.BR003.getValue()));
    }

    @Test
    public void should_not_transfer_money_for_non_active_receiver_account() {

        TransferDto transferDto = new TransferDto(1000, 3000, BigDecimal.valueOf(30), 1);

        given().body(transferDto)
                .when().post("http://localhost:" + ApiConstants.APPLICATION_PORT + ApiConstants.API_URL + "/transfer")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("success", equalTo(false))
                .body("error.code", equalTo(RevolutResponseCode.BR004.name()))
                .body("error.description", equalTo(RevolutResponseCode.BR004.getValue()));
    }

    @Test
    public void should_not_transfer_money_for_funds_insufficient() {

        TransferDto transferDto = new TransferDto(1000, 2000, BigDecimal.valueOf(999999999999L), 1);

        given().body(transferDto)
                .when().post("http://localhost:" + ApiConstants.APPLICATION_PORT + ApiConstants.API_URL + "/transfer")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST_400)
                .contentType(ContentType.JSON)
                .assertThat()
                .body("success", equalTo(false))
                .body("error.code", equalTo(RevolutResponseCode.BR005.name()))
                .body("error.description", equalTo(RevolutResponseCode.BR005.getValue()));
    }


    @AfterClass
    public static void teardown() throws SQLException {
        connection.close();
        start.stop();
    }
}

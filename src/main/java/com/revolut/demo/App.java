package com.revolut.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.demo.controller.AccountController;
import com.revolut.demo.dto.response.TransferResponseDto;
import com.revolut.demo.exception.OptimisticLockException;
import com.revolut.demo.exception.RevolutBusinessException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.ContentType;
import org.eclipse.jetty.http.HttpStatus;
import org.flywaydb.core.Flyway;
import org.jooq.tools.jdbc.SingleConnectionDataSource;

import static com.revolut.demo.config.DatasourceConfig.getConnection;
import static com.revolut.demo.constant.ApiConstants.API_URL;
import static com.revolut.demo.constant.ApiConstants.APPLICATION_PORT;
import static com.revolut.demo.dto.response.TransferResponseDto.CustomErrorMessage;
import static io.javalin.apibuilder.ApiBuilder.*;


public class App {

    public static void main(String[] args) {
        getStart();
    }

    public static Javalin getStart() {
        Javalin app = Javalin.create(config -> {
            config.enableCorsForAllOrigins();
            config.defaultContentType = "application/json";
        }).routes(() -> {
            // Transfer Endpoint
            path(API_URL + "/transfer", () -> {
                post(AccountController.transferMoney); // New money transfer request
            });

            //Transaction Details Endpoint
            path(API_URL + "/transactions", () -> {
                path(":trx_id", () -> {
                    get(AccountController.getTrxDetails);
                });
            });

        }).start(APPLICATION_PORT);

        // For H2 IN MEMORY db
        Flyway flyway = Flyway.configure().dataSource(new SingleConnectionDataSource(getConnection())).load();
        flyway.migrate();

        // Handle Revolut Business Exception
        app.exception(RevolutBusinessException.class, App::handleExceptions);
        app.exception(OptimisticLockException.class, App::handleExceptions);


        return app;
    }


    private static void handleExceptions(Exception e, Context ctx) {

        var error = new TransferResponseDto();
        error.setSuccess(false);

        if (e instanceof RevolutBusinessException) {
            RevolutBusinessException e1 = (RevolutBusinessException) e;
            error.setError(new CustomErrorMessage(e1.getResponseCode().name(), e1.getResponseCode().getValue()));
        } else if (e instanceof OptimisticLockException) {
            OptimisticLockException e2 = (OptimisticLockException) e;
            error.setError(new CustomErrorMessage(e2.getMessage()));
        }

        String errorJson = "";
        try {
            errorJson = new ObjectMapper().writeValueAsString(error);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        ctx.status(HttpStatus.BAD_REQUEST_400).result(errorJson).contentType(ContentType.JSON);
    }


}

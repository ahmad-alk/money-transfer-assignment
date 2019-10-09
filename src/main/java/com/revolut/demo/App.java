package com.revolut.demo;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.demo.controller.AccountController;
import com.revolut.demo.dto.TransferResponseDto;
import com.revolut.demo.exception.OptimisticLockException;
import com.revolut.demo.exception.RevolutBusinessException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.ContentType;
import org.eclipse.jetty.http.HttpStatus;

import static com.revolut.demo.constant.ApiConstants.API_URL;
import static com.revolut.demo.constant.ApiConstants.APPLICATION_PORT;
import static com.revolut.demo.dto.TransferResponseDto.CustomErrorMessage;


public class App {

    public static void main(String[] args) {
        getStart();
    }

    public static Javalin getStart() {
        Javalin app = Javalin.create().start(APPLICATION_PORT);

        // Handle Revolut Business Exception
        app.exception(RevolutBusinessException.class, App::handleExceptions);
        app.exception(OptimisticLockException.class, App::handleExceptions);

        // Transfer Endpoint
        app.post(API_URL + "/transfer", AccountController.transferMoney);

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

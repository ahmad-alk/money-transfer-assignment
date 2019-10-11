package com.revolut.demo.controller;

import com.revolut.demo.dto.request.TransferDto;
import com.revolut.demo.dto.response.TransactionDetailsResponse;
import com.revolut.demo.service.AccountService;
import com.revolut.demo.service.AccountServiceImpl;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import org.jooq.tools.StringUtils;

import java.util.Set;

public class AccountController {

    private static AccountService accountService = new AccountServiceImpl();

    public static Handler transferMoney =
            ctx -> {
                if (StringUtils.isEmpty(ctx.body())) {
                    ctx.result("Empty object !").status(HttpStatus.BAD_REQUEST_400);
                    return;
                }
                TransferDto transferDto = ctx.bodyAsClass(TransferDto.class);
                accountService.transferMoney(transferDto);

                ctx.status(HttpStatus.OK_200);
            };

    public static Handler getTrxDetails =
            ctx -> {
                String trx_id = ctx.pathParam("trx_id");
                if (StringUtils.isEmpty(trx_id)) {
                    ctx.result("Transaction id cannot be null !").status(HttpStatus.BAD_REQUEST_400);
                    return;
                }
                Set<TransactionDetailsResponse> transactionDetailsResponses = accountService.transactionDetails(trx_id);

                if (transactionDetailsResponses.isEmpty()) {
                    ctx.status(HttpStatus.NO_CONTENT_204);
                } else {
                    ctx.json(transactionDetailsResponses).status(HttpStatus.OK_200);
                }
            };

}

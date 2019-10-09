package com.revolut.demo.controller;

import com.revolut.demo.dto.TransferDto;
import com.revolut.demo.service.AccountService;
import com.revolut.demo.service.AccountServiceImpl;
import io.javalin.http.Handler;
import org.eclipse.jetty.http.HttpStatus;
import org.jooq.tools.StringUtils;

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

                ctx.result("Created").status(HttpStatus.OK_200);
            };

}

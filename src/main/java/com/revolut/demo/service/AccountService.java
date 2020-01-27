package com.revolut.demo.service;

import com.revolut.demo.dto.request.TransferDto;
import com.revolut.demo.dto.response.TransactionDetailsResponse;
import com.revolut.demo.exception.RevolutBusinessException;

import java.util.Set;

public interface AccountService {

    void transferMoney(TransferDto transferDto) throws RevolutBusinessException;

    Set<TransactionDetailsResponse> transactionDetails(String trx_id);
}

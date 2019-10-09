package com.revolut.demo.service;

import com.revolut.demo.dto.TransferDto;
import com.revolut.demo.exception.RevolutBusinessException;

public interface AccountService {

    // Add Business code
    void transferMoney(TransferDto transferDto) throws RevolutBusinessException;


}

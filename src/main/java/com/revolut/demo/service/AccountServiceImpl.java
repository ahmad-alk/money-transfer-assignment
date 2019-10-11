package com.revolut.demo.service;

import com.revolut.demo.dao.AccountDao;
import com.revolut.demo.dao.AccountDaoImpl;
import com.revolut.demo.dto.TransferDto;
import com.revolut.demo.exception.RevolutBusinessException;
import com.revolut.demo.jooq.model.revolut_schema.tables.records.AccountRecord;

import static com.revolut.demo.constant.RevolutResponseCode.*;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao = AccountDaoImpl.getInstance();

    @Override
    public void transferMoney(TransferDto transferDto) throws RevolutBusinessException {

        // These cross cutting concerns would usually be managed outside of the service
        if (!accountDao.authorizedUserAccount(transferDto))
            throw new RevolutBusinessException(BR001);

        if (transferDto.getAcc_from() == transferDto.getAcc_to())
            throw new RevolutBusinessException(BR002);

        AccountRecord accountFrom = accountDao.findById(transferDto.getAcc_from());
        AccountRecord accountTo = accountDao.findById(transferDto.getAcc_to());

        if (!accountDao.activeAccount(accountFrom)) throw new RevolutBusinessException(BR003);

        if (!accountDao.activeAccount(accountTo)) throw new RevolutBusinessException(BR004);

        if (!accountDao.isAvailableBalance(accountFrom, transferDto.getAmount()))
            throw new RevolutBusinessException(BR005);

        accountDao.transfer(accountFrom, accountTo, transferDto.getAmount());
    }
}

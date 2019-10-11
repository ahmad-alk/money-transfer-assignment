package com.revolut.demo.service;

import com.revolut.demo.constant.RevolutResponseCode;
import com.revolut.demo.dao.AccountDao;
import com.revolut.demo.dto.request.TransferDto;
import com.revolut.demo.exception.RevolutBusinessException;
import com.revolut.demo.jooq.model.revolut_schema.tables.records.AccountRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void should_not_transfer_money_for_unauthorized_user() {
        // Given
        TransferDto transferDto = new TransferDto(1000, 1000, BigDecimal.valueOf(30), 1);

        // When
        Mockito.doReturn(false).when(accountDao).authorizedUserAccount(transferDto);
        when(accountService).transferMoney(transferDto);

        // Then
        then(caughtException()).isInstanceOf(RevolutBusinessException.class).hasMessage(RevolutResponseCode.BR001.toString());
    }

    @Test
    public void should_not_transfer_money_for_illegal_transaction() {
        // Given
        TransferDto transferDto = new TransferDto(1000, 1000, BigDecimal.valueOf(30), 1);

        // When
        Mockito.doReturn(true).when(accountDao).authorizedUserAccount(transferDto);
        when(accountService).transferMoney(transferDto);

        // Then
        then(caughtException()).isInstanceOf(RevolutBusinessException.class).hasMessage(RevolutResponseCode.BR002.toString());
    }

    @Test
    public void should_not_transfer_money_for_non_active_sender_account() {
        // Given
        TransferDto transferDto = new TransferDto(3000, 1000, BigDecimal.valueOf(30), 3);

        // When
        Mockito.doReturn(true).when(accountDao).authorizedUserAccount(transferDto);
        when(accountService).transferMoney(transferDto);

        // Then
        then(caughtException()).isInstanceOf(RevolutBusinessException.class).hasMessage(RevolutResponseCode.BR003.toString());
    }

    @Test
    public void should_not_transfer_money_for_non_active_receiver_account() {
        // Given
        TransferDto transferDto = new TransferDto(1000, 3000, BigDecimal.valueOf(30), 1);
        AccountRecord accountFrom = new AccountRecord(1000L, BigDecimal.valueOf(300L), Short.valueOf("1"), "test", new Timestamp(100L), 1, 1);
        AccountRecord accountTo = new AccountRecord(2000L, BigDecimal.valueOf(100L), Short.valueOf("1"), "test", new Timestamp(100L), 1, 2);

        // When
        Mockito.doReturn(true).when(accountDao).authorizedUserAccount(transferDto);
        Mockito.doReturn(accountFrom).when(accountDao).findById(transferDto.getAcc_from());
        Mockito.doReturn(accountTo).when(accountDao).findById(transferDto.getAcc_to());
        Mockito.doReturn(true).when(accountDao).activeAccount(accountFrom);
        Mockito.doReturn(false).when(accountDao).activeAccount(accountTo);
        when(accountService).transferMoney(transferDto);

        // Then
        then(caughtException()).isInstanceOf(RevolutBusinessException.class).hasMessage(RevolutResponseCode.BR004.toString());
    }

    @Test
    public void should_not_transfer_money_for_funds_insufficient() {
        // Given
        TransferDto transferDto = new TransferDto(1000, 3000, BigDecimal.valueOf(30000), 1);
        AccountRecord accountFrom = new AccountRecord(1000L, BigDecimal.valueOf(300L), Short.valueOf("1"), "test", new Timestamp(100L), 1, 1);
        AccountRecord accountTo = new AccountRecord(2000L, BigDecimal.valueOf(100L), Short.valueOf("1"), "test", new Timestamp(100L), 1, 2);

        // When
        Mockito.doReturn(true).when(accountDao).authorizedUserAccount(transferDto);
        Mockito.doReturn(accountFrom).when(accountDao).findById(transferDto.getAcc_from());
        Mockito.doReturn(accountTo).when(accountDao).findById(transferDto.getAcc_to());
        Mockito.doReturn(true).when(accountDao).activeAccount(accountFrom);
        Mockito.doReturn(true).when(accountDao).activeAccount(accountTo);
        Mockito.doReturn(false).when(accountDao).isAvailableBalance(accountFrom, transferDto.getAmount());
        when(accountService).transferMoney(transferDto);

        // Then
        then(caughtException()).isInstanceOf(RevolutBusinessException.class).hasMessage(RevolutResponseCode.BR005.toString());
    }

    @Test
    public void should_return_transaction_details() {

    }

}

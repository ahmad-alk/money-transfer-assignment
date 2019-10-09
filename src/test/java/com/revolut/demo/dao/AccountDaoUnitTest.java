package com.revolut.demo.dao;

import com.revolut.demo.dto.TransferDto;
import com.revolut.demo.jooq.model.revolut_db_4.tables.records.AccountRecord;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.tools.jdbc.MockConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.revolut.demo.jooq.model.revolut_db_4.Tables.ACCOUNT;
import static org.jooq.impl.DSL.using;

@RunWith(MockitoJUnitRunner.class)
public class AccountDaoUnitTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DSLContext database;
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private Context ctx;
    @Spy
    @InjectMocks
    AccountDaoImpl accountDao;


    @Ignore // FIXME
    @Test
    public void should_transfer_successfully() throws SQLException {
        // Given
        TransferDto transferDto = new TransferDto(1000, 3000, 30, 1);
        AccountRecord accountFrom = new AccountRecord(1000L, 3000L, Short.valueOf("1"), "test", new Timestamp(100L), 10, 1);
        AccountRecord accountTo = new AccountRecord(2000L, 1000L, Short.valueOf("1"), "test", new Timestamp(100L), 20, 2);
        MockConfiguration mockConfiguration = new MockConfiguration(null, null);
        Integer amount = transferDto.getAmount();

        // When
        Mockito.doReturn(accountFrom.getAccVersion(), accountFrom.getAccVersion() + 1).when(accountDao).setOptimisticLockForAccount(accountFrom);
        Mockito.doReturn(accountTo.getAccVersion(), accountTo.getAccVersion() + 1).when(accountDao).setOptimisticLockForAccount(accountTo);


        BDDMockito.given(using(mockConfiguration)
                .update(ACCOUNT).set(ACCOUNT.ACC_BALANCE, accountTo.getAccBalance() - amount)
                .where(ACCOUNT.ACC_NO.eq(accountTo.getAccNo())
                        .and(ACCOUNT.ACC_VERSION.eq(1)))
                .execute()).willReturn(1);

        BDDMockito.given(using(mockConfiguration)
                .update(ACCOUNT).set(ACCOUNT.ACC_BALANCE, accountFrom.getAccBalance() - amount)
                .where(ACCOUNT.ACC_NO.eq(accountFrom.getAccNo())
                        .and(ACCOUNT.ACC_VERSION.eq(1)))
                .execute()).willReturn(1);
        accountDao.startTransfer(mockConfiguration, accountFrom, accountTo, amount, 1, 1);


    }
}

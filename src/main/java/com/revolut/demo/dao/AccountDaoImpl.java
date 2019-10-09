package com.revolut.demo.dao;

import com.revolut.demo.config.DatasourceConfig;
import com.revolut.demo.dto.TransferDto;
import com.revolut.demo.exception.OptimisticLockException;
import com.revolut.demo.jooq.model.revolut_db_4.tables.records.AccountRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.revolut.demo.jooq.model.revolut_db_4.Tables.ACCOUNT;
import static org.jooq.SQLDialect.H2;
import static org.jooq.impl.DSL.using;

public class AccountDaoImpl implements AccountDao {

    private DataSource dataSource = DatasourceConfig.getInstance();
    private Connection connection = DatasourceConfig.getConnection();
    private DSLContext db = using(dataSource, H2);
    private static AccountDaoImpl accountDAO = null;


    /**
     * Initializes a newly created {@code AccountDao} object.
     */
    public static AccountDaoImpl getInstance() {
        if (null == accountDAO) accountDAO = new AccountDaoImpl();
        return accountDAO;
    }

    @Override
    public AccountRecord findById(long acc_no) {
        return db.selectFrom(ACCOUNT).where(ACCOUNT.ACC_NO.eq(acc_no)).fetchOne();
    }

    @Override
    public boolean isAvailableBalance(AccountRecord account, Integer amount) {
        return amount <= account.getAccBalance();
    }

    @Override
    public boolean authorizedUserAccount(TransferDto transferDto) {
        Result<AccountRecord> fetch =
                db.selectFrom(ACCOUNT)
                        .where(ACCOUNT.ACC_NO.eq(transferDto.getAcc_from())
                                .and(ACCOUNT.USER_ID.eq(transferDto.getUser_id())))
                        .fetch();
        return fetch.isNotEmpty();
    }

    @Override
    public boolean activeAccount(AccountRecord account) {
        return account.getAccActive() != 0;
    }

    @Override
    public void transfer(AccountRecord accountFrom, AccountRecord accountTo, Integer amount) {
        try {
            Integer versionAccFrom = setOptimisticLockForAccount(accountFrom);
            Integer versionAccTo = setOptimisticLockForAccount(accountTo);

            db.transaction(new TransactionalRunnable() {
                @Override
                public void run(Configuration configuration) throws Throwable {
                    AccountDaoImpl.this.startTransfer(configuration, accountFrom, accountTo, amount, versionAccFrom, versionAccTo);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void startTransfer(Configuration configuration, AccountRecord accountFrom, AccountRecord accountTo, Integer amount, Integer versionAccFrom, Integer versionAccTo) {
        int updatedRecordsFromAccount =
                DSL.using(configuration)
                        .update(ACCOUNT)
                        .set(ACCOUNT.ACC_BALANCE, accountFrom.getAccBalance() - amount)
                        .where(ACCOUNT.ACC_NO.eq(accountFrom.getAccNo())
                                .and(ACCOUNT.ACC_VERSION.eq(versionAccFrom)))
                        .execute();

        int updatedRecordsToAccount =
                using(configuration)
                        .update(ACCOUNT)
                        .set(ACCOUNT.ACC_BALANCE, accountTo.getAccBalance() + amount)
                        .where(ACCOUNT.ACC_NO.eq(accountTo.getAccNo())
                                .and(ACCOUNT.ACC_VERSION.eq(versionAccTo)))
                        .execute();

        if (updatedRecordsFromAccount <= 0 || updatedRecordsToAccount <= 0) {
            throw new OptimisticLockException();
        }
    }

    Integer setOptimisticLockForAccount(AccountRecord account) throws SQLException {
        Integer version = account.get(ACCOUNT.ACC_VERSION) + 1;
        db.update(ACCOUNT)
                .set(ACCOUNT.ACC_VERSION, version)
                .where(ACCOUNT.ACC_NO.eq(account.getAccNo()))
                .execute();
        connection.commit();
        return version;
    }
}

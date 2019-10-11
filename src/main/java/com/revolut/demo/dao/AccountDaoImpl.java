package com.revolut.demo.dao;

import com.revolut.demo.config.DatasourceConfig;
import com.revolut.demo.dto.request.TransferDto;
import com.revolut.demo.dto.response.TransactionDetailsResponse;
import com.revolut.demo.exception.OptimisticLockException;
import com.revolut.demo.jooq.model.revolut_schema.tables.records.AccountRecord;
import com.revolut.demo.jooq.model.revolut_schema.tables.records.AccountTransactionsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.revolut.demo.constant.TransactionStatus.FAILED;
import static com.revolut.demo.constant.TransactionStatus.SUCCESS;
import static com.revolut.demo.jooq.model.revolut_schema.Tables.ACCOUNT;
import static com.revolut.demo.jooq.model.revolut_schema.Tables.ACCOUNT_TRANSACTIONS;
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
    public boolean isAvailableBalance(AccountRecord account, BigDecimal amount) {
        return amount.compareTo(account.getAccBalance()) <= 0;
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
    public void transfer(AccountRecord accountFrom, AccountRecord accountTo, BigDecimal amount) {

        // Start Transaction
        AtomicBoolean success = new AtomicBoolean(true);
        db.transaction(configuration -> {

            DSL.using(configuration)
                    .update(ACCOUNT)
                    .set(ACCOUNT.ACC_BALANCE, accountFrom.getAccBalance().subtract(amount))
                    .where(ACCOUNT.ACC_NO.eq(accountFrom.getAccNo()))
                    .execute();


            using(configuration)
                    .update(ACCOUNT)
                    .set(ACCOUNT.ACC_BALANCE, accountTo.getAccBalance().add(amount))
                    .where(ACCOUNT.ACC_NO.eq(accountTo.getAccNo()))
                    .execute();


            boolean lock1 = setOptimisticLockForAccount(accountFrom, configuration);
            boolean lock2 = setOptimisticLockForAccount(accountTo, configuration);

            if (!lock1 || !lock2) {
                success.set(false);
                throw new OptimisticLockException();
            }
        });

        if (success.get()) {
            db.insertInto(ACCOUNT_TRANSACTIONS).columns(ACCOUNT_TRANSACTIONS.AT_ID, ACCOUNT_TRANSACTIONS.AT_FROM, ACCOUNT_TRANSACTIONS.AT_TO, ACCOUNT_TRANSACTIONS.AT_AMOUNT, ACCOUNT_TRANSACTIONS.AT_STATUS, ACCOUNT_TRANSACTIONS.AT_REMARKS)
                    .values(UUID.randomUUID().toString(), accountFrom.getAccNo(), accountTo.getAccNo(), amount, SUCCESS.name(), "").execute();
        } else {
            db.insertInto(ACCOUNT_TRANSACTIONS).columns(ACCOUNT_TRANSACTIONS.AT_ID, ACCOUNT_TRANSACTIONS.AT_FROM, ACCOUNT_TRANSACTIONS.AT_TO, ACCOUNT_TRANSACTIONS.AT_AMOUNT, ACCOUNT_TRANSACTIONS.AT_STATUS, ACCOUNT_TRANSACTIONS.AT_REMARKS)
                    .values(UUID.randomUUID().toString(), accountFrom.getAccNo(), accountTo.getAccNo(), amount, FAILED.name(), "").execute();

        }

    }

    @Override
    public Set<TransactionDetailsResponse> getTransactionDetails(String trx_id) {
        Result<Record> transactions = db.select().from(ACCOUNT_TRANSACTIONS).where(ACCOUNT_TRANSACTIONS.AT_ID.eq(trx_id)).fetch();

        return transactions.stream().map(transaction -> {
            AccountTransactionsRecord transactionRecord = (AccountTransactionsRecord) transaction;

            TransactionDetailsResponse transactionDetailsResponse = new TransactionDetailsResponse();
            transactionDetailsResponse.setId(transactionRecord.getAtId());
            transactionDetailsResponse.setAcc_from("" + transactionRecord.getAtFrom());
            transactionDetailsResponse.setAcc_to("" + transactionRecord.getAtTo());
            transactionDetailsResponse.setAmount(transactionRecord.getAtAmount().toPlainString());
            transactionDetailsResponse.setRemarks(transactionRecord.getAtRemarks());
            transactionDetailsResponse.setDate(transactionRecord.getAtDate().toString());
            transactionDetailsResponse.setStatus(transactionRecord.getAtStatus());
            return transactionDetailsResponse;
        }).collect(Collectors.toSet());
    }


    boolean setOptimisticLockForAccount(AccountRecord account, Configuration configuration) throws SQLException {
        int version = account.get(ACCOUNT.ACC_VERSION) + 1;
        Record1<Integer> dbVersion = using(configuration).select(ACCOUNT.ACC_VERSION).from(ACCOUNT).where(ACCOUNT.ACC_NO.eq(account.getAccNo())).fetchAny();

        if (dbVersion.value1() + 1 == version) {
            using(configuration).update(ACCOUNT)
                    .set(ACCOUNT.ACC_VERSION, version)
                    .where(ACCOUNT.ACC_NO.eq(account.getAccNo()))
                    .execute();
            return true;
        }
        return false;
    }

}

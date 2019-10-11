package com.revolut.demo.dao;

import com.revolut.demo.dto.TransferDto;
import com.revolut.demo.jooq.model.revolut_schema.tables.records.AccountRecord;

import java.math.BigDecimal;

/**
 * Interface for generic CRUD operations for
 *
 * <p>AccountDao .
 */
public interface AccountDao {

    /**
     * Retrieves an entity by its id.
     *
     * @param acc_no account number
     * @return AccountRecord if an entity with the given id exists.
     */
    AccountRecord findById(long acc_no);

    /**
     * @param account sender account
     * @param amount  amount to send
     * @return true if account.availableBalance > amount, false otherwise.
     */
    boolean isAvailableBalance(AccountRecord account, BigDecimal amount);

    /**
     * Check if the user_id owns this account_no, transfer money from his account not other.
     *
     * @param transferDto
     * @return true, false
     */
    boolean authorizedUserAccount(TransferDto transferDto);

    /**
     * @param account sender/receiver account
     * @return true if the account is active, false otherwise
     */
    boolean activeAccount(AccountRecord account);

    /**
     * Start money transfer transaction.
     *
     * @param accountFrom sender account
     * @param accountTo   receiver account
     * @param amount      amount to send
     * @throws {@link com.revolut.demo.exception.OptimisticLockException}
     */
    void transfer(AccountRecord accountFrom, AccountRecord accountTo, BigDecimal amount);
}

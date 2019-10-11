/*
 * This file is generated by jOOQ.
 */
package com.revolut.demo.jooq.model.revolut_schema.tables.records;


import com.revolut.demo.jooq.model.revolut_schema.tables.AccountTransactions;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.12.1"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class AccountTransactionsRecord extends UpdatableRecordImpl<AccountTransactionsRecord> implements Record7<String, Long, Long, BigDecimal, String, Timestamp, String> {

    private static final long serialVersionUID = -1860912765;

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_ID</code>.
     */
    public void setAtId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_ID</code>.
     */
    public String getAtId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_FROM</code>.
     */
    public void setAtFrom(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_FROM</code>.
     */
    public Long getAtFrom() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_TO</code>.
     */
    public void setAtTo(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_TO</code>.
     */
    public Long getAtTo() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_AMOUNT</code>.
     */
    public void setAtAmount(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_AMOUNT</code>.
     */
    public BigDecimal getAtAmount() {
        return (BigDecimal) get(3);
    }

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_STATUS</code>.
     */
    public void setAtStatus(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_STATUS</code>.
     */
    public String getAtStatus() {
        return (String) get(4);
    }

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_DATE</code>.
     */
    public void setAtDate(Timestamp value) {
        set(5, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_DATE</code>.
     */
    public Timestamp getAtDate() {
        return (Timestamp) get(5);
    }

    /**
     * Setter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_REMARKS</code>.
     */
    public void setAtRemarks(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS.AT_REMARKS</code>.
     */
    public String getAtRemarks() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, Long, Long, BigDecimal, String, Timestamp, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, Long, Long, BigDecimal, String, Timestamp, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_ID;
    }

    @Override
    public Field<Long> field2() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_FROM;
    }

    @Override
    public Field<Long> field3() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_TO;
    }

    @Override
    public Field<BigDecimal> field4() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_AMOUNT;
    }

    @Override
    public Field<String> field5() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_STATUS;
    }

    @Override
    public Field<Timestamp> field6() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_DATE;
    }

    @Override
    public Field<String> field7() {
        return AccountTransactions.ACCOUNT_TRANSACTIONS.AT_REMARKS;
    }

    @Override
    public String component1() {
        return getAtId();
    }

    @Override
    public Long component2() {
        return getAtFrom();
    }

    @Override
    public Long component3() {
        return getAtTo();
    }

    @Override
    public BigDecimal component4() {
        return getAtAmount();
    }

    @Override
    public String component5() {
        return getAtStatus();
    }

    @Override
    public Timestamp component6() {
        return getAtDate();
    }

    @Override
    public String component7() {
        return getAtRemarks();
    }

    @Override
    public String value1() {
        return getAtId();
    }

    @Override
    public Long value2() {
        return getAtFrom();
    }

    @Override
    public Long value3() {
        return getAtTo();
    }

    @Override
    public BigDecimal value4() {
        return getAtAmount();
    }

    @Override
    public String value5() {
        return getAtStatus();
    }

    @Override
    public Timestamp value6() {
        return getAtDate();
    }

    @Override
    public String value7() {
        return getAtRemarks();
    }

    @Override
    public AccountTransactionsRecord value1(String value) {
        setAtId(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord value2(Long value) {
        setAtFrom(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord value3(Long value) {
        setAtTo(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord value4(BigDecimal value) {
        setAtAmount(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord value5(String value) {
        setAtStatus(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord value6(Timestamp value) {
        setAtDate(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord value7(String value) {
        setAtRemarks(value);
        return this;
    }

    @Override
    public AccountTransactionsRecord values(String value1, Long value2, Long value3, BigDecimal value4, String value5, Timestamp value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AccountTransactionsRecord
     */
    public AccountTransactionsRecord() {
        super(AccountTransactions.ACCOUNT_TRANSACTIONS);
    }

    /**
     * Create a detached, initialised AccountTransactionsRecord
     */
    public AccountTransactionsRecord(Integer atId, Long atFrom, Long atTo, BigDecimal atAmount, String atStatus, Timestamp atDate, String atRemarks) {
        super(AccountTransactions.ACCOUNT_TRANSACTIONS);

        set(0, atId);
        set(1, atFrom);
        set(2, atTo);
        set(3, atAmount);
        set(4, atStatus);
        set(5, atDate);
        set(6, atRemarks);
    }
}
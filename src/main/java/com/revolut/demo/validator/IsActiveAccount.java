package com.revolut.demo.validator;

import com.revolut.demo.jooq.model.revolut_db_4.tables.records.AccountRecord;

import java.util.function.Predicate;

public class IsActiveAccount implements Predicate<AccountRecord> {


    @Override
    public boolean test(AccountRecord accountRecord) {
        return accountRecord.getAccActive() != 0;
    }
}

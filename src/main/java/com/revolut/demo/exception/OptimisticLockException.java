package com.revolut.demo.exception;

/**
 * Thrown by the persistence provider when an optimistic locking conflict occurs.This exception may
 * be thrown as part of an API call, a flush or at commit time. The current transaction, if one is
 * active, will be marked for
 *
 * <h5>rollback</h5>
 * <p>
 * .
 */
public class OptimisticLockException extends RuntimeException {

    public OptimisticLockException() {
        super("OptimisticLockException");
    }
}

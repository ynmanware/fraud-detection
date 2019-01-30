package com.afterpay.cc.frauddetection.exception;

/**
 * This exception thrown when one or more parameters of the transaction are missing
 *
 * @author Yogesh
 */
public class InvalidTransactionException extends FraudDetectionApplicationException {

    public InvalidTransactionException(String msg) {
        super(msg);
    }
}

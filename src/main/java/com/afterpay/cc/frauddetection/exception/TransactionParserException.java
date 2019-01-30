package com.afterpay.cc.frauddetection.exception;

/**
 * This exceptions is thrown when there is an exception while parsing the transaction parameters
 *
 * @author Yogesh
 */
public class TransactionParserException extends FraudDetectionApplicationException {
    public TransactionParserException(String msg) {
        super(msg);
    }
}

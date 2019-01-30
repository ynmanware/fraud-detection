package com.afterpay.cc.frauddetection.exception;

/**
 * All Fraud Detection Application related exceptions must extend this exception
 *
 * @author Yogesh
 */
class FraudDetectionApplicationException extends RuntimeException {
    FraudDetectionApplicationException(String msg) {
        super(msg);
    }
}

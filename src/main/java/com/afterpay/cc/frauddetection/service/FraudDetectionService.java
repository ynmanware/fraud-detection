package com.afterpay.cc.frauddetection.service;

import com.afterpay.cc.frauddetection.domain.Request;

import java.util.Set;

/**
 *
 * @author Yogesh
 */
interface FraudDetectionService {
    Set<String> detect(Request request);
}

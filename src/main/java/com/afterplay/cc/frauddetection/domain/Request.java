package com.afterpay.cc.frauddetection.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 *
 * This is an input to the Fraud Detection service
 * @author Yogesh
 *
 */
public class Request {
    private final List<String> transactions;
    private final LocalDate date;
    private final BigDecimal thresholdAmount;

    public Request(List<String> transactions, LocalDate date, BigDecimal thresholdAmount) {
        this.transactions = transactions;
        this.date = date;
        this.thresholdAmount = thresholdAmount;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getThresholdAmount() {
        return thresholdAmount;
    }

}

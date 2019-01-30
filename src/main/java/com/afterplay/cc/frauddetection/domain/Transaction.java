package com.afterpay.cc.frauddetection.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * This class represents a credit card transaction
 *
 * @author Yogesh
 */
public class Transaction {
    private final String creditCardNumber; //hashed
    private final LocalDateTime date;
    private final BigDecimal amount;

    public Transaction(String creditCardNumber, LocalDateTime date, BigDecimal amount) {
        this.creditCardNumber = creditCardNumber;
        this.date = date;
        // always round to two decimal places
        this.amount = amount.setScale(2, RoundingMode.CEILING);
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}

package com.afterpay.cc.frauddetection.service;

import com.afterpay.cc.frauddetection.domain.Request;
import com.afterpay.cc.frauddetection.domain.Transaction;
import com.afterpay.cc.frauddetection.service.util.TransactionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a core of Fraud Detection application.
 * This class is responsible for iterating over all transaction and find the fraudulent transactions and relevant card Number
 *
 * @author Yogesh
 */
@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {
    private static final Logger logger = LoggerFactory.getLogger(FraudDetectionServiceImpl.class);

    @Override
    public Set<String> detect(Request request) {
        logger.info("started finding fraud transactions .....");

        //translate transaction strings to transaction object
        logger.info("parsing transactions .....");
        List<Transaction> transactionsList = TransactionParser.parse(request.getTransactions());
        logger.info("parsing transactions completed.....");

        //find all transactions on the given date grouped by card number
        Map<String, List<Transaction>> transactionsByCardNumber = new HashMap<>(transactionsList.stream().filter(transaction ->
                transaction.getDate().toLocalDate().equals(request.getDate())).collect(Collectors.groupingBy(Transaction::getCreditCardNumber)));

        // calculate sum of amounts and compare with threshold amount
        Set<String> cardNumbersHavingFraudTransactions = transactionsByCardNumber.entrySet().stream().filter(entry -> {
            BigDecimal total = entry.getValue().stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            return total.compareTo(request.getThresholdAmount()) > 0;
        }).map(Map.Entry::getKey).collect(Collectors.toSet());

        logger.info("finding fraud transactions completed.....");

        if (logger.isDebugEnabled()) {
            String result = Arrays.toString(cardNumbersHavingFraudTransactions.toArray());
            logger.debug("Card numbers having fraud transactions are - {} ", result);
        }

        return cardNumbersHavingFraudTransactions;
    }
}

package com.afterpay.cc.frauddetection.service.util;

import com.afterpay.cc.frauddetection.domain.Transaction;
import com.afterpay.cc.frauddetection.exception.InvalidTransactionException;
import com.afterpay.cc.frauddetection.exception.TransactionParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for parsing the list of transaction from String to Transaction
 *
 * @author Yogesh
 */
public class TransactionParser {
    private static final Logger logger = LoggerFactory.getLogger(TransactionParser.class);
    private static final String COMMA = ",";
    private static final String SPACE = "\\s*";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private TransactionParser() {
    }

    public static List<Transaction> parse(List<String> transactionStr) {
        return transactionStr.stream().map(TransactionParser::parse).collect(Collectors.toList());
    }

    public static Transaction parse(String transactionStr) {
        //check for any empty parameter
        emptyTransactionCheck(transactionStr);

        //remove all unnecessary spaces
        transactionStr = transactionStr.replaceAll(SPACE, "");

        String[] transactionArr = transactionStr.split(COMMA);

        //parse date
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(transactionArr[1], dateTimeFormatter);
        } catch (Exception ex) {
            logger.error("Could not parse the transaction date: " + transactionArr[1], ex);
            throw new TransactionParserException("Could not parse the transaction date");
        }

        //parse amount
        BigDecimal amount;
        try {
            amount = new BigDecimal(transactionArr[2]);
        } catch (Exception ex) {
            logger.error("Could not parse the transaction amount: " + transactionArr[2], ex);
            throw new TransactionParserException("Could not parse the transaction amount");
        }

        return new Transaction(transactionArr[0], localDateTime, amount);
    }


    private static void emptyTransactionCheck(String transactionStr) {
        emptyStringCheck(transactionStr, "Transaction line is empty");
        String[] transactionArr = transactionStr.split(COMMA);
        if (transactionArr.length < 3) {
            logger.error("One or more parameters in the given transaction are missing: {}", transactionStr);
            throw new InvalidTransactionException("One or more parameters in the given transaction are missing");
        }
        Arrays.stream(transactionArr).forEach(parameter ->
                emptyStringCheck(parameter, "One or more parameters in the transaction are missing")
        );
    }

    private static void emptyStringCheck(String target, String message) {
        if (StringUtils.isEmpty(target)) {
            throw new InvalidTransactionException(message);
        }
    }
}
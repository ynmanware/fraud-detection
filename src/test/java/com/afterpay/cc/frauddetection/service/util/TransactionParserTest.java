package com.afterpay.cc.frauddetection.service.util;

import com.afterpay.cc.frauddetection.domain.Transaction;
import com.afterpay.cc.frauddetection.exception.InvalidTransactionException;
import com.afterpay.cc.frauddetection.exception.TransactionParserException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yogesh
 */
public class TransactionParserTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testParseTransaction_basic_date_amount() {
        Transaction transaction = TransactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.15");
        Assert.assertEquals(LocalDateTime.of(2014, 4, 29, 13, 15, 54), transaction.getDate());
        Assert.assertTrue(new BigDecimal("10.15").equals(transaction.getAmount()));
    }

    @Test
    public void testParseTransaction_basic_ceiling() {
        Transaction transaction = TransactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.1523");
        Assert.assertEquals(LocalDateTime.of(2014, 4, 29, 13, 15, 54), transaction.getDate());
        Assert.assertTrue(new BigDecimal("10.16").equals(transaction.getAmount()));
    }


    @Test
    public void testParseTransactions_basic_txn() {
        List<String> transactions = new ArrayList<>();
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54, 15.00");
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54, 25.00");
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-05-29T13:15:54, 35.00");

        List<Transaction> transactionList = TransactionParser.parse(transactions);
        Assert.assertTrue(transactionList.size() == 3);
    }

    @Test
    public void testTransactionParserException_invalidAmount() {
        exception.expect(TransactionParserException.class);
        exception.expectMessage("Could not parse the transaction amount");
        List<String> transactions = new ArrayList<>();
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54, amount");
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54, 25.00");
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-05-29T13:15:54, 35.00");
        TransactionParser.parse(transactions);
    }

    @Test
    public void testParseTransaction_invalidDate() {
        exception.expect(TransactionParserException.class);
        exception.expectMessage("Could not parse the transaction date");
        TransactionParser.parse("10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T00:00.00, 10.1523");
    }

    @Test
    public void testInvalidTransactionException_blankParameter() {
        exception.expect(InvalidTransactionException.class);
        exception.expectMessage("One or more parameters in the transaction are missing");
        List<String> transactions = new ArrayList<>();
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54, 15.00");
        transactions.add(", 2018-04-29T13:15:54, 25.00");
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-05-29T13:15:54, 35.00");
        TransactionParser.parse(transactions);
    }

    @Test
    public void testInvalidTransactionException_emptyTxn() {
        exception.expect(InvalidTransactionException.class);
        exception.expectMessage("Transaction line is empty");
        List<String> transactions = new ArrayList<>();
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54, 15.00");
        transactions.add("");
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-05-29T13:15:54, 35.00");
        TransactionParser.parse(transactions);
    }

    @Test
    public void testInvalidTransactionException_missingParameter() {
        exception.expect(InvalidTransactionException.class);
        exception.expectMessage("One or more parameters in the given transaction are missing");
        List<String> transactions = new ArrayList<>();
        transactions.add("10d7ce2f43e35fa57d1bbf8b1e2, 2018-04-29T13:15:54");
        TransactionParser.parse(transactions);
    }
}

package com.afterpay.cc.frauddetection.service;

import com.afterpay.cc.frauddetection.domain.Request;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Yogesh
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class FraudDetectionServiceImplTest {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private FraudDetectionService fraudDetectionService;

    /* basic tests with simple transactions */
    @Test
    public void testDetectFraud_basic() {
        //prepare request
        Request request = new Request(TransactionGenerator.generate(5), LocalDate.now().plusDays(1), new BigDecimal("10"));
        Set<String> cardNumbers = fraudDetectionService.detect(request);
        Assert.assertTrue(!cardNumbers.isEmpty());
    }

    /**
     * detect fraud on two card numbers
     */
    @Test
    public void testDetectFraud_positive() {
        List<String> card1 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,200.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,300.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-02T16:13:18,200.00");
        List<String> card2 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,50.00", "10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,100.00", "10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,200.00");
        List<String> card3 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,101.00", "10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,100.00", "10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,200.00");
        List<String> transactions = new ArrayList<>();
        transactions.addAll(card1);
        transactions.addAll(card2);
        transactions.addAll(card3);

        Request request = new Request(transactions, LocalDate.parse("2018-06-01", dateFormatter), new BigDecimal("400.00"));
        Set<String> cardNumbers = fraudDetectionService.detect(request);
        Set<String> cardNumberExp = new HashSet<>(Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1", "10d7ce2f43e35fa57d1bbf8b1e3"));
        Assert.assertTrue(cardNumberExp.equals(cardNumbers));
    }

    /**
     * detect fraud on with minor over threshold
     */
    @Test
    public void testDetectFraud_minorAmount_positive() {
        List<String> card1 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,200.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,300.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-02T16:13:18,200.00");
        List<String> card2 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,50.00", "10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,100.00", "10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,200.00");
        List<String> card3 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,100.01", "10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,100.00", "10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,200.00");
        List<String> transactions = new ArrayList<>();
        transactions.addAll(card1);
        transactions.addAll(card2);
        transactions.addAll(card3);
        Request request = new Request(transactions, LocalDate.parse("2018-06-01", dateFormatter), new BigDecimal("400.00"));
        Set<String> cardNumbers = fraudDetectionService.detect(request);
        Set<String> cardNumberExp = new HashSet<>(Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1", "10d7ce2f43e35fa57d1bbf8b1e3"));
        Assert.assertTrue(cardNumberExp.equals(cardNumbers));
    }


    /**
     * No frauds
     */
    @Test
    public void testDetectFraud_negative() {
        List<String> card1 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,200.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,300.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-02T16:13:18,200.00");
        List<String> card2 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,50.00", "10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,100.00", "10d7ce2f43e35fa57d1bbf8b1e2,2018-06-01T16:13:18,200.00");
        List<String> card3 = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,101.00", "10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,100.00", "10d7ce2f43e35fa57d1bbf8b1e3,2018-06-01T16:13:18,200.00");
        List<String> transactions = new ArrayList<>();
        transactions.addAll(card1);
        transactions.addAll(card2);
        transactions.addAll(card3);
        Request request = new Request(transactions, LocalDate.parse("2018-06-01", dateFormatter), new BigDecimal("500.00"));
        Assert.assertTrue(fraudDetectionService.detect(request).isEmpty());
    }

    /**
     * single transaction fraud
     */
    @Test
    public void testDetectFraud_oneTxn_positive() {
        List<String> transactions = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,10000.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,200.00");
        Request request = new Request(transactions, LocalDate.parse("2018-06-01", dateFormatter), new BigDecimal("500.00"));
        Assert.assertTrue(!fraudDetectionService.detect(request).isEmpty());
    }

    /***
     *  random test with high number of transactions
     */
    @Test
    public void testDetectFraud_highTxns() {
        Request request = new Request(TransactionGenerator.generate(1000000), LocalDate.now(), new BigDecimal("500.00"));
        Assert.assertTrue(!fraudDetectionService.detect(request).isEmpty());
    }

    /**
     * test with huge amount
     */
    @Test
    public void testHugeAmount() {
        List<String> transactions = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,10000000000.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,200.00");
        Request request = new Request(transactions, LocalDate.parse("2018-06-01", dateFormatter), new BigDecimal("500.00"));
        Assert.assertTrue(!fraudDetectionService.detect(request).isEmpty());
    }

    /**
     * test with huge threshold
     */
    @Test
    public void testHugeThreshold() {
        List<String> transactions = Arrays.asList("10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,1000.00", "10d7ce2f43e35fa57d1bbf8b1e1,2018-06-01T16:13:18,200.00");
        Request request = new Request(transactions, LocalDate.parse("2018-06-01", dateFormatter), new BigDecimal("50000000000.00"));
        Assert.assertTrue(fraudDetectionService.detect(request).isEmpty());
    }
}

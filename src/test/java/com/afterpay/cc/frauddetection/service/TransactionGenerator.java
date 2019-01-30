package com.afterpay.cc.frauddetection.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yogesh
 */
class TransactionGenerator {
    private static final String[] cardNumberList = {"10d7ce2f43e35fa57d1bbf8b1e2", "10d7ce2f43e35fa45d1bbf8b1e3", "10d7ce2f43e35fa57d1bbf8b1e4"};
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static List<String> generate(long count) {
        {
            List<String> transactions = new ArrayList<>();
            Arrays.stream(cardNumberList).forEach(cardNumber -> {
                int[] i = new int[1];
                transactions.addAll(Stream.generate(() ->
                        new StringBuilder(cardNumber).append(",").append(LocalDateTime.now().plusDays(i[0]++ / 3).format(dateTimeFormatter)).append(",").append(new BigDecimal(Math.random() * 1000000).setScale(2, RoundingMode.CEILING).toString()).toString()
                ).limit(count).collect(Collectors.toList()));
            });

            return transactions;
        }
    }
}

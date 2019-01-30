#  Fraud Detection Service
+ This is a simple maven based Spring Boot application
+ Uses Java 8 features  
+ The application contains a service that accepts list of transactions in a String format (e.g.  '10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00'), Date  and Threshold
+ It calculates the sum of transaction amount on the given date and compares with the given Threshold
+ It returns all card numbers that have sum greater than the given Threshold
+ The core of application is in FraudDetectionServiceImpl
+ To know more about the service and how to use it, you may want to refer Test cases


*To run the test cases with sample inputs, use maven command as below.* 

````
mvn test
````

The test internally uses random transaction generator along with other combinations. 
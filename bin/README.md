# url-validator

## Instructions to run the project

### Pre-requisites
1. Java 8 is installed in the machine. To check go to command prompt type java -version.
2. VPN connectivity to the BI-DB is working and connection is established from any sql client
3. Eclipse or STS editor is present

### How to execute the code

#### Test Data:
1. The sku data is stored in a file known as the skulist.txt. This is under src/main/resources. Please paste the sku list here.


#### Execution:
1. To execute
	a. From Eclipse, go to the class UrlValidatorApplication.java . Right click and run as Spring Boot app for STS or Java application
	b. From command line use the command ./gradlew run
2. The code is executed and the execution is logged on console.
3. Once execution is completed you get the message like 

	```
	2020-02-13 13:42:41.968  INFO 11454 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'executor'
	2020-02-13 13:42:41.969  INFO 11454 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
	2020-02-13 13:42:41.977  INFO 11454 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.

	BUILD SUCCESSFUL in 2m 20s ```
	
#### Results:
1. The results are stored in a csv format in the folder src/main/resources. The output file name is output.csv.
2. The file contains the URL , HTTPStatus error code.


### Things to do

1.  The service that checks the http status codes is single threaded. Need to make it multi-threaded.
2.  Jenkins job creation where the user can input the list of SKU's
3.  Verifying the template data from S3 bucket to reduce the load on http status service.
4.  Better logging, right now the logging is using System.out.
5.  Credential management - BiDB.
# url-validator

## Instructions to run the project

### Pre-requisites
1. Java 8 is installed in the machine. To check go to command prompt type java -version.
2. VPN connectivity to the BI-DB is working and connection is established from any sql client
3. Eclipse or STS editor is present

### How to execute the code

#### Test Data:
1. The sku data is stored in a file known as the skulist.txt. This is under src/main/resources. Please paste the sku list here.

#### DB Connection properties
Connection to DB is provided in application.properties under src/main/resources folder. The file has three properties related to DB
1. Host, user and password.

To connect to the new BIDB RDS Aurora servers from this application do the following
1. Request IT to get access to the new servers
2. Open a SSH tunnel from localhost to the new Server

Command
```
	ssh -f <ssh user>@<bastion ip> -L localhost:3306:<BiDB server host/ip>:3306 -N -i <pem file>.
```
	Execute the above command from the directory where the pem file is stored or provide the path to the pem file in the above command.
3. Update the above properties file with localhost as DB host and 3306 as the port.
4. Provide the credentials and run the app	
	

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


Scope
-----
The scope of the challenge, this prototype 
maintains customer account balances based on incoming transaction data.  it is assumed that 
there is some kind of existing account management/banking system within the
organisation that this application could potentially interact with in the future.

Design rationale
-----------------
From a high level perspective, the application could be designed in two ways:

- As a long-running daemon process that contains its own scheduling mechanism;
- As a one-shot program that is triggered by some external scheduler.

It has been decided to choose the former scheme, as it is more suited to a
self-contained application design and will be simpler to deploy. However, the 
system should be decoupled such that it can be easily reconfigured to use the 
latter scheme.

Separation of concerns
-----------------------
In general, the system should have a clear separation of concerns between logical
chunks. The following distinct areas of operation have been identified:

- Scheduling of processing job execution;
- High-level control flow orchestration;
- Parsing of customer transaction files;
- Application of transactions to customer accounts;
- Storage of customer accounts and their credit/debit amounts;
- Filesystem related methods (reading/writing/copying files).

Each of these concerns should be implemented separately and not coupled to one 
another.

Implementation
--------------
The application has been written in Java (1.8) . The source tree is structured using 
the standard Maven project layout. Dependency management is also handled via Maven.

The Spring framework (most notably Spring Boot) is used throughout the application.
Spring Boot uses a "convention over configuration" approach to minimise boilerplate code
and allow rapid application development.

Lombok is also employed to remove the need to write getters/setters, constructors, etc.

 Architecture
--------------

The main entry point of the application is the 
[`Main`](src/main/java/Main.java) class. This class is
annotated as a `SpringBootApplication` which will instruct the Spring framework to 
instantiate its context and wire up any `@Component` classes found on the classpath.

The [`Scheduler`](src/main/java/Scheduler.java) class is
responsible for executing scheduled jobs. It makes use of Spring's scheduling support
(namely the `@EnableScheduling` and `@Scheduled` annotations) which makes it very simple 
to ensure scheduled method execution with very little plumbing.

The scheduler makes the following assumptions:

* The time zone is UTC
* Transaction files take one minute or less to be received

Therefore, the scheduler runs at 06:01am and 21:01pm UTC each day. This ensures that 
processing commences within 5 minutes of delivery.

The scheduler delegates to the 
[`TransactionProcessor`](src/main/java/TransactionProcessor.java)
class, which encapsulates the high-level control flow orchestration. The processing happens 
in four distinct stages:

1. Reading all pending customer transaction files
2. Applying new transactions to customer accounts
3. Writing a report file for each processed file
4. Archiving each processed file

The `TRANSACTION_PROCESSING` environment variable, which is referenced in 
[application.properties](src/main/resources/application.properties), is automatically picked
up by Spring to determine the location of customer transaction files. This is another 
example of a "convention over configuration" approach that helps to reduce boilerplate code. 
If this variable is missing, the application will fail to start.

The `TransactionProcessor` makes use of the 
[`TransactionFileParser`](src/main/java/com/java/fileparser/TransactionFileParser.java)
which encapsulates the logic for parsing customer transactions from the data files 
(and skipping corrupt lines). Each customer transaction file is loaded into a data structure 
called [`TransactionFile`](src/main/java/com/java/transaction/TransactionFile.java).

The newly loaded transaction files are then passed to the 
[`CustomerAccountService`](src/main/java/com/java/customer/CustomerAccountTransaction.java)
which maintains a map of 
[`CustomerAccount`](src/main/java/com/java/customer/CustomerAccount.java) 
instances which record the current balance for a single customer account. New transactions 
are then applied to their corresponding accounts. The customer account service can be queried
to retrieve balances for individual accounts.

[`FileUtils`](src/main/java/com/java/util/FileUtils.java) encapsulates
all filesystem related methods (reading/writing/moving files).

 Exception handling strategy
-----------------------------
As a general rule, any checked exceptions are wrapped and re-thrown as unchecked 
exceptions, which are caught and logged at the topmost level.

Performance
-----------
The average processing time for a file with 500,000 transactions is currently around
~900ms. This can potentially be optimised by improving the performance of 
`FileUtils#readLinesFromFile`. 

It would be possible to parallelise the processing of transaction files, however it would
be necessary to synchronise on the modification of customer account balances.

Testing strategy
----------------
As a general rule, all methods that contain logic are unit tested. The main processing 
control flow logic is integration tested with the entire application.


 Scalability
--------------
The most major factor that affects the scalability of this prototype is the fact that it
is stateful. It holds customer account balances in-memory and has no provisions for 
shared storage. Therefore, when the application dies, so does all the data.

This limitation could be solved by storing data in some kind of database or using some
kind of distributed cache (such as Ehcache, Redis, Hazelcast etc.). However, this was 
deliberately left out of scope for this prototype.

Given an adequate solution to the above, and assuming that the transaction file delivery
mechanism is capable of balancing the delivery across multiple machines, it would be 
relatively simple to scale the application (as long as each instance runs on a separate 
machine to avoid file contention).



How to run the application
---------------------------
Requirements: Java 1.8, Maven 3

Ensure the `$TRANSACTION_PROCESSING` variable is set on the system, then run the following:

```
$ mvn package
$ java -jar target/transaction-processor*.jar
```
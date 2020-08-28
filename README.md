OData Spring Boot + Batch Example
=================================

- Run the `odata-service` through Maven or in your IDE:

```
cd odata-service
mvn clean install
mvn -f example-service/pom.xml spring-boot:run
```

Verify that the OData Service is running correctly:

```
http --json http://localhost:8080/rabobank.svc/People
```

- Run the `odata-client` through Maven or in your IDE:

```
cd odata-client
mvn spring-boot:run
```

Verify that the data has been processed in the client correctly:

```
http --json http://localhost:8082/people
```

- Run the `odata-batch` through Maven or in your IDE:

```
cd odata-batch
mvn spring-boot:run
```

You should see the client and the batch application both querying the OData Service using the OLingo java libary.
They both achieve the same thing, but in a different way.

Check for more information on OLingo
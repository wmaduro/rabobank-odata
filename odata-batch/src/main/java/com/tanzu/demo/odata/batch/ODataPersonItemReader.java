package com.tanzu.demo.odata.batch;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.commons.api.format.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.olingo.client.core.ODataClientFactory.getClient;

public class ODataPersonItemReader extends AbstractPagingItemReader<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ODataPersonItemReader.class);

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        }
        else {
            results.clear();
        }

        ODataClient client = getClient();
        client.getConfiguration().setDefaultPubFormat(ContentType.APPLICATION_JSON);

        int skip = this.getPageSize() * this.getPage();
        int top = this.getPageSize();

        LOGGER.info("Querying Person: skip {} and take top {}", skip, top);
        ODataEntitySetRequest<ClientEntitySet> request = client.getRetrieveRequestFactory()
                .getEntitySetRequest(client.newURIBuilder("http://localhost:8080/rabobank.svc")
                        .appendEntitySetSegment("Persons")
                        .addQueryOption("count", "true", true)
                        .addQueryOption("skip", "" + skip, true)
                        .addQueryOption("top", "" + top, true)
                        .build());
        ODataRetrieveResponse<ClientEntitySet> response = request.execute();
        ClientEntitySet entitySet = response.getBody();

        LOGGER.info("Found {} on a total of {} people.", entitySet.getEntities().size(), entitySet.getCount().toString());
        AtomicInteger imported = new AtomicInteger();

        entitySet.getEntities().forEach(entity -> {
            Person person = new Person();
            person.setRabobankId(entity.getProperty("id").getValue().toString());
            person.setFirstName(entity.getProperty("firstName").getValue().toString());
            person.setLastName(entity.getProperty("lastName").getValue().toString());
            person.setAge(Integer.parseInt(entity.getProperty("age").getValue().toString()));

            this.results.add(person);
        });
    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }
}

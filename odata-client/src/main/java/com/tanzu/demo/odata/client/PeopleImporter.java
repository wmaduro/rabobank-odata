package com.tanzu.demo.odata.client;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.commons.api.format.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.olingo.client.core.ODataClientFactory.getClient;

@Component
public class PeopleImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleImporter.class);

    private final PersonRepository personRepository;

    public PeopleImporter(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void importPeople() {
        ODataClient client = getClient();
        client.getConfiguration().setDefaultPubFormat(ContentType.APPLICATION_JSON);

        ODataEntitySetRequest<ClientEntitySet> request = client.getRetrieveRequestFactory()
                .getEntitySetRequest(client.newURIBuilder("http://localhost:8080/rabobank.svc")
                        .appendEntitySetSegment("Persons")
                        .addQueryOption("count", "true", true)
                        .addQueryOption("skip", "0", true)
                        .addQueryOption("top", "100", true)
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

            if (!this.personRepository.existsByRabobankId(person.getRabobankId())) {
                LOGGER.debug("Importing {} {}", person.getFirstName(), person.getLastName());
                this.personRepository.save(person);
                imported.addAndGet(1);
            }
        });

        LOGGER.info("Imported [{}] people", imported.get());
    }
}

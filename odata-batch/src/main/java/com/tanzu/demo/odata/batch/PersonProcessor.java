package com.tanzu.demo.odata.batch;

import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) {
        final Person transformedPerson = new Person();
        transformedPerson.setFirstName(person.getFirstName().toUpperCase());
        transformedPerson.setLastName(person.getLastName().toUpperCase());
        transformedPerson.setRabobankId(person.getRabobankId());

        return transformedPerson;
    }
}

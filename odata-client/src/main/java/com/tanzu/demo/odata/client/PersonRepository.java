package com.tanzu.demo.odata.client;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByRabobankId(String rabobankId);
}

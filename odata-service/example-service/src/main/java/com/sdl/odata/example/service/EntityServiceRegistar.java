/**
 * Copyright (c) 2015 SDL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sdl.odata.example.service;

import com.google.common.collect.Lists;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.registry.ODataEdmRegistry;
import com.sdl.odata.example.Person;
import com.sdl.odata.example.datasource.InMemoryDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class EntityServiceRegistar {
    private static final Logger LOG = LoggerFactory.getLogger(EntityServiceRegistar.class);
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final ODataEdmRegistry oDataEdmRegistry;
    private final InMemoryDataSource inMemoryDataSource;

    @Value("classpath:firstnames.txt")
    private Resource firstNamesFile;

    @Value("classpath:lastnames.txt")
    private Resource lastNamesFile;

    @Autowired
    public EntityServiceRegistar(ODataEdmRegistry oDataEdmRegistry, InMemoryDataSource inMemoryDataSource) {
        this.oDataEdmRegistry = oDataEdmRegistry;
        this.inMemoryDataSource = inMemoryDataSource;
    }

    @PostConstruct
    public void registerEntities() throws ODataException, IOException {
        LOG.info("Registering Rabobank Entity");

        oDataEdmRegistry.registerClasses(Lists.newArrayList(
                Person.class,
                GetAverageAge.class
        ));

        importDemoData();
    }

    private void importDemoData() throws IOException {
        List<String> firstNames = Files.lines(Paths.get(firstNamesFile.getURI())).collect(Collectors.toList());
        Collections.shuffle(firstNames);

        List<String> lastNames = Files.lines(Paths.get(lastNamesFile.getURI())).collect(Collectors.toList());
        Collections.shuffle(lastNames);

        firstNames.stream().filter(StringUtils::hasText).limit(100).flatMap(firstName ->
                lastNames.stream().filter(StringUtils::hasText).limit(100)
                        .map(lastName -> new Person("RABO-" + StringUtils.trimAllWhitespace(firstName) + StringUtils.trimAllWhitespace(lastName), firstName, lastName, RANDOM.nextInt(100)))
        ).forEach(person -> {
            try {
                LOG.info("Creating Person [{}]", person.getPersonId());
                inMemoryDataSource.create(null, person, null);
            } catch (ODataException e) {
                LOG.error("Could not create person [" + ((Person) person).getPersonId() + "]");
            }
        });
    }
}

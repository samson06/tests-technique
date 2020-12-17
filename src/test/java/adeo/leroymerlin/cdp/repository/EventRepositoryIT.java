/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : EventRepositoryTest.java
 * Date de création : 16 déc. 2020
 * Heure de création : 13:12:28
 * Package : adeo.leroymerlin.cdp.repository
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */	
package adeo.leroymerlin.cdp.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.MyEventTestsUtils;
import adeo.leroymerlin.cdp.config.MyEventBaseConfig;
import adeo.leroymerlin.cdp.model.Band;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.model.Member;
import adeo.leroymerlin.cdp.util.MyEventUtils;

/**
 * Integration Tests Class for {@link EventRepository}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:application-test.yml" })
@ContextConfiguration(name = "eventRepositoryIT", classes = { MyEventBaseConfig.class })
@ActiveProfiles("test")
@Sql(scripts = { "classpath:scripts/schema.sql", "classpath:scripts/data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
@Transactional
public class EventRepositoryIT
{
    //
    @Autowired
    private EventRepository eventRepository;

    /**
     * Test method for {@link adeo.leroymerlin.cdp.repository.EventRepository#findAllBy()}.
     */
    @Test
    public void testFindAllBy()
    {
        assertThat(this.eventRepository.findAllBy().size()).isEqualTo(5);// we have 5 event records in the data.sql initialization file
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.repository.EventRepository#save(adeo.leroymerlin.cdp.model.Event)}.
     */
    @Test
    public void testSave()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEvent();
        final Event savedEvent = this.eventRepository.save(eventToSaved);

        assertThat(savedEvent).isNotNull();
        assertThat(this.eventRepository.findAllBy().size()).isEqualTo(6); // we have 5 event records in the data.sql initialization file and add one
    }


    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testSaveShouldThrowException()
    {
        final Event event = this.eventRepository.save(null);

        assertThat(event).isNull();
    }


    /**
     * Test method for {@link adeo.leroymerlin.cdp.repository.EventRepository#delete(java.lang.Long)}.
     */
    @Test
    public void testDelete()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEvent();
        final Event savedEvent = this.eventRepository.save(eventToSaved);

        assertThat(savedEvent).isNotNull();
        assertThat(savedEvent.getId()).isNotNull();

        this.eventRepository.delete(savedEvent.getId());

        assertThat(this.eventRepository.findAllBy().size()).isEqualTo(5); // we have 5 event records in the data.sql initialization file
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testDeleteShouldThrowException()
    {
        this.eventRepository.delete(null);
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.repository.EventRepository#findOneWithBandsById(java.lang.Long)}.
     */
    @Test
    public void testFindOneWithBandsById()
    {
        // Retrieve Event
        final Optional<Event> optional = this.eventRepository.findOneById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // Get Event
        final Event event = optional.get();

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(event.getBands());

        assertThat(event.getTitle()).isEqualTo(MyEventTestsUtils.TITLE_REPO_TEST);
        assertThat(event.getNbStars()).isNull();
        assertThat(event.getComment()).isNull();
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(1);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.NAME_REPO_TEST); // 1006

        // Get Band Member List
        final List<Member> members = MyEventUtils.convertSetToList(bands.get(0).getMembers());

        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(4);
        assertThat(members.get(0).getName()).isEqualTo(MyEventTestsUtils.MEMBER_NAME_REPO_TEST); // 1021
    }

    @Test
    public void testFindOneWithBandsById_EnsureOptionalIsNotPresent()
    {
        final Optional<Event> optional = this.eventRepository.findOneById(MyEventTestsUtils.NON_EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void testFindOneWithBandsById_EnsureOptionalIsNotPresentWithNull()
    {
        final Optional<Event> optional = this.eventRepository.findOneById(null);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isFalse();
    }
}

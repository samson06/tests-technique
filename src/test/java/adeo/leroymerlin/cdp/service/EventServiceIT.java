/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : EventServiceTest.java
 * Date de création : 16 déc. 2020
 * Heure de création : 21:18:39
 * Package : adeo.leroymerlin.cdp.service
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */	
package adeo.leroymerlin.cdp.service;

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
import adeo.leroymerlin.cdp.error.MyEventCustomException;
import adeo.leroymerlin.cdp.model.Band;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.model.Member;
import adeo.leroymerlin.cdp.util.MyEventUtils;

/**
 * Integration Tests Class for {@link EventService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:application-test.yml" })
@ContextConfiguration(name = "eventServiceIT", classes = { MyEventBaseConfig.class, EventService.class })
@ActiveProfiles("test")
@Sql(scripts = { "classpath:scripts/schema.sql", "classpath:scripts/data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
@Transactional
public class EventServiceIT
{
    //
    @Autowired
    private EventService eventService;

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#getEvents()}.
     */
    @Test
    public void testGetEvents()
    {
        final List<Event> resultEvents = this.eventService.getEvents();
        assertThat(resultEvents).isNotNull();
        assertThat(resultEvents.size()).isEqualTo(5); // we have 5 event records in the data.sql initialization file
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#createEvent(adeo.leroymerlin.cdp.model.Event)}.
     */
    @Test
    public void testCreateEvent()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEvent();
        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        assertThat(this.eventService.getEvents().size()).isEqualTo(6); // we have 5 event records in the data.sql initialization file and add one
    }

    @Test
    public void testCreateEvent_NullBand()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEventNullBand();
        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        assertThat(this.eventService.getEvents().size()).isEqualTo(6); // we have 5 event records in the data.sql initialization file and add one
        assertThat(this.eventService.getEvents().get(5).getBands()).isNull();
    }

    @Test
    public void testCreateEvent_NullMember()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEventNullMember();
        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        assertThat(this.eventService.getEvents().size()).isEqualTo(6); // we have 5 event records in the data.sql initialization file and add one
        assertThat(MyEventUtils.convertSetToList(this.eventService.getEvents().get(5).getBands()).get(0).getMembers()).isNull();
    }

    @Test(expected = MyEventCustomException.class)
    public void testCreateEvent_ShouldThrowException()
    {
        final Event event = this.eventService.createEvent(null);

        assertThat(event).isNull();
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#delete(java.lang.Long)}.
     */
    @Test
    public void testDelete()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEvent();
        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        assertThat(savedEvent.getId()).isNotNull();

        this.eventService.delete(savedEvent.getId());

        assertThat(this.eventService.getEvents().size()).isEqualTo(5);// we have 5 event records in the data.sql initialization file
    }


    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testDelete_ShouldThrowException()
    {
        this.eventService.delete(null);
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#findEventById(java.lang.Long)}.
     */
    @Test
    public void testFindEventById()
    {
        // Retrieve Event
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

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
    public void testFindEventById_EnsureOptionalIsNotPresent()
    {
        Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.NON_EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isFalse();
    }

    @Test
    public void testFindEventById_EnsureOptionalIsNotPresentWithNull()
    {
        Optional<Event> optional = this.eventService.findEventById(null);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isFalse();
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#updateEvent(java.lang.Long, adeo.leroymerlin.cdp.model.Event)}.
     */
    @Test
    public void testUpdateEvent()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEvent();
        
        // Retrieve Event
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // Get Event
        final Event event = optional.get();
        assertThat(event).isNotNull();
        
        this.eventService.updateEvent(event.getId(), eventToUpdated);

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventToUpdated.getBands());

        assertThat(eventToUpdated.getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventToUpdated.getNbStars()).isEqualTo(5);
        assertThat(eventToUpdated.getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(eventToUpdated.getImgUrl()).containsSequence(MyEventTestsUtils.URL_PATTERN);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(1);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.BAND_NAME);

        // Get Band Member List
        final List<Member> members = MyEventUtils.convertSetToList(bands.get(0).getMembers());

        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getName()).isEqualTo(MyEventTestsUtils.MEMBER_NAME);
    }

    @Test
    public void testUpdateEvent_WithNullBand()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEventNullBand();

        // Retrieve Event
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // Get Event
        final Event event = optional.get();
        assertThat(event).isNotNull();

        this.eventService.updateEvent(event.getId(), eventToUpdated);

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventToUpdated.getBands());

        assertThat(eventToUpdated.getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventToUpdated.getNbStars()).isEqualTo(5);
        assertThat(eventToUpdated.getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(eventToUpdated.getImgUrl()).containsSequence(MyEventTestsUtils.URL_PATTERN);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUpdateEvent_WithNullBandShouldThrowException()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEventNullBand();

        // Retrieve Event
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // Get Event
        final Event event = optional.get();
        assertThat(event).isNotNull();

        this.eventService.updateEvent(event.getId(), eventToUpdated);

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventToUpdated.getBands());

        assertThat(eventToUpdated.getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventToUpdated.getNbStars()).isEqualTo(5);
        assertThat(eventToUpdated.getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(eventToUpdated.getImgUrl()).containsSequence(MyEventTestsUtils.URL_PATTERN);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(0);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.BAND_NAME);
    }

    @Test
    public void testUpdateEvent_WithNullMember()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEventNullMember();

        // Retrieve Event
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // Get Event
        final Event event = optional.get();
        assertThat(event).isNotNull();

        this.eventService.updateEvent(event.getId(), eventToUpdated);

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventToUpdated.getBands());

        assertThat(eventToUpdated.getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventToUpdated.getNbStars()).isEqualTo(5);
        assertThat(eventToUpdated.getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(eventToUpdated.getImgUrl()).containsSequence(MyEventTestsUtils.URL_PATTERN);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(1);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.BAND_NAME);

        // Get Band Member List
        final List<Member> members = MyEventUtils.convertSetToList(bands.get(0).getMembers());

        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUpdateEvent_WithNullMemberShouldThrowException()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEventNullMember();

        // Retrieve Event
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // Get Event
        final Event event = optional.get();
        assertThat(event).isNotNull();

        this.eventService.updateEvent(event.getId(), eventToUpdated);

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventToUpdated.getBands());

        assertThat(eventToUpdated.getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventToUpdated.getNbStars()).isEqualTo(5);
        assertThat(eventToUpdated.getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(eventToUpdated.getImgUrl()).containsSequence(MyEventTestsUtils.URL_PATTERN);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(1);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.BAND_NAME);

        // Get Band Member List
        final List<Member> members = MyEventUtils.convertSetToList(bands.get(0).getMembers());

        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(0);
        assertThat(members.get(0).getName()).isEqualTo(MyEventTestsUtils.MEMBER_NAME);
    }

    @Test
    public void testUpdateEvent_WithNullId()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEvent();
        this.eventService.updateEvent(null, eventToUpdated);

        assertThat(eventToUpdated).isNotNull();
        assertThat(eventToUpdated.getId()).isNull();
    }

    @Test
    public void testUpdateEvent_FullNull()
    {
        this.eventService.updateEvent(null, null);

    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#getFilteredEvents(java.lang.String)}.
     */
    @Test
    public void testGetFilteredEvents()
    {
        // XXX: MAJ UNE FOIS LA FONCTION COMPLETEE

        final List<Event> events = this.eventService.getFilteredEvents(null);

        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(5);
    }
}

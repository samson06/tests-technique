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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import adeo.leroymerlin.cdp.MyEventTestsUtils;
import adeo.leroymerlin.cdp.config.MyEventBaseConfig;
import adeo.leroymerlin.cdp.error.MyEventCustomException;
import adeo.leroymerlin.cdp.model.Band;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.model.Member;
import adeo.leroymerlin.cdp.repository.EventRepository;
import adeo.leroymerlin.cdp.util.MyEventUtils;

/**
 * Unit Tests Class for {@link EventService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:application-test.yml" })
@ContextConfiguration(name = "eventServiceTest", classes = { MyEventBaseConfig.class, EventService.class })
@ActiveProfiles("test")
@SpringBootTest
public class EventServiceTest
{
    @MockBean
    private EventRepository eventRepository; // creation of a mock Bean for EventRepository

    //
    @Autowired
    private EventService eventService;

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#getEvents()}.
     */
    @Test
    public void testGetEvents()
    {
        final Event event = MyEventTestsUtils.buildTestEvent();
        final Event event1 = MyEventTestsUtils.buildTestEventNullBand();
        final Event event2 = MyEventTestsUtils.buildTestEventNullMember();

        final List<Event> events = new ArrayList<>();
        events.add(event);
        events.add(event1);
        events.add(event2);
        
        when(this.eventRepository.findAllBy()).thenReturn(events);

        final List<Event> resultEvents = this.eventService.getEvents();
        assertThat(resultEvents).isNotNull();
        assertThat(resultEvents.size()).isEqualTo(3);
        verify(eventRepository).findAllBy();
    }

    @Test
    public void testGetEvents_ShouldReturnNull()
    {
        when(this.eventRepository.findAllBy()).thenReturn(null);

        final List<Event> resultEvents = this.eventService.getEvents();
        assertThat(resultEvents).isNull();
        verify(eventRepository).findAllBy();
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#createEvent(adeo.leroymerlin.cdp.model.Event)}.
     */
    @Test
    public void testCreateEvent()
    {
        final Event eventMock = MyEventTestsUtils.buildTestEvent();
        final Event eventToSaved = MyEventTestsUtils.buildTestEvent();

        when(this.eventRepository.save(eventToSaved)).thenReturn(eventMock);

        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        MyEventTestsUtils.AssertAllEvent(eventMock, savedEvent);
        verify(this.eventRepository).save(any(Event.class));
    }

    @Test
    public void testCreateEvent_NullBand()
    {
        final Event eventMock = MyEventTestsUtils.buildTestEventNullBand();
        final Event eventToSaved = MyEventTestsUtils.buildTestEventNullBand();

        when(this.eventRepository.save(eventToSaved)).thenReturn(eventMock);
        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        MyEventTestsUtils.AssertAllEvent(eventMock, savedEvent);
        verify(this.eventRepository).save(any(Event.class));
    }

    @Test
    public void testCreateEvent_NullMember()
    {
        final Event eventMock = MyEventTestsUtils.buildTestEventNullMember();
        final Event eventToSaved = MyEventTestsUtils.buildTestEventNullMember();

        when(this.eventRepository.save(eventToSaved)).thenReturn(eventMock);
        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNotNull();
        MyEventTestsUtils.AssertAllEvent(eventMock, savedEvent);
        verify(this.eventRepository).save(any(Event.class));
    }

    @Test(expected = MyEventCustomException.class)
    public void testCreateEvent_ShouldThrowException()
    {
        final Event eventToSaved = MyEventTestsUtils.buildTestEventNullMember();
        when(this.eventRepository.save(eventToSaved)).thenReturn(null);

        final Event savedEvent = this.eventService.createEvent(eventToSaved);

        assertThat(savedEvent).isNull();
        verify(this.eventRepository).save(any(Event.class));
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#delete(java.lang.Long)}.
     */
    @Test
    public void testDelete()
    {
        final Event eventMock = MyEventTestsUtils.buildTestEvent();
        final Event eventToDeleted = MyEventTestsUtils.buildTestEvent();

        when(this.eventRepository.save(eventToDeleted)).thenReturn(eventMock);
        final Event savedEvent = this.eventService.createEvent(eventToDeleted);

        assertThat(savedEvent).isNotNull();
        assertThat(savedEvent.getId()).isNull();
        MyEventTestsUtils.AssertAllEvent(eventMock, savedEvent);

        this.eventService.delete(savedEvent.getId());
        verify(this.eventRepository).delete(any(Long.class));
    }


    @Test
    public void testDelete_WithNull()
    {
        this.eventService.delete(null);
        verify(this.eventRepository).delete(any(Long.class));
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#findEventById(java.lang.Long)}.
     */
    @Test
    public void testFindEventById()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEvent());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event event = optional.get();
        event.setId(1L);

        when(this.eventRepository.findOneById(event.getId())).thenReturn(Optional.of(event));

        final Optional<Event> eventFromDB = this.eventService.findEventById(event.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isTrue();

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventFromDB.get().getBands());

        assertThat(eventFromDB.get().getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventFromDB.get().getNbStars()).isEqualTo(5);
        assertThat(eventFromDB.get().getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(1);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.BAND_NAME);

        // Get Band Member List
        final List<Member> members = MyEventUtils.convertSetToList(bands.get(0).getMembers());

        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getName()).isEqualTo(MyEventTestsUtils.MEMBER_NAME);

        verify(this.eventRepository).findOneById(any(Long.class));
    }

    @Test
    public void testFindEventById_EnsureOptionalIsNotPresent()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEvent());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event event = optional.get();
        event.setId(1L);

        when(this.eventRepository.findOneById(event.getId())).thenReturn(Optional.empty());
        final Optional<Event> eventFromDB = this.eventService.findEventById(event.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isFalse();
        verify(eventRepository).findOneById(any(Long.class));
    }

    @Test(expected = MyEventCustomException.class)
    public void testFindEventById_ShouldThrowException()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEvent());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event event = optional.get();
        event.setId(1L);

        when(this.eventRepository.findOneById(event.getId())).thenReturn(null);
        final Optional<Event> eventFromDB = this.eventService.findEventById(event.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isFalse();
        verify(this.eventRepository).findOneById(any(Long.class));
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.service.EventService#updateEvent(java.lang.Long, adeo.leroymerlin.cdp.model.Event)}.
     */
    @Test
    public void testUpdateEvent()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEvent());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();
        eventToUpdated.setId(1L);

        when(this.eventRepository.save(eventToUpdated)).thenReturn(eventToUpdated);
        when(this.eventRepository.findOneById(eventToUpdated.getId())).thenReturn(Optional.of(eventToUpdated));
        final Optional<Event> eventFromDB = this.eventService.findEventById(eventToUpdated.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isTrue();
        
        this.eventService.updateEvent(eventFromDB.get().getId(), eventToUpdated);

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

        verify(this.eventRepository).save(any(Event.class));
        verify(this.eventRepository, times(2)).findOneById(any(Long.class));
    }

    @Test
    public void testUpdateEvent_WithNullBand()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEventNullBand());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();
        eventToUpdated.setId(1L);

        when(this.eventRepository.save(eventToUpdated)).thenReturn(eventToUpdated);
        when(this.eventRepository.findOneById(eventToUpdated.getId())).thenReturn(Optional.of(eventToUpdated));
        final Optional<Event> eventFromDB = this.eventService.findEventById(eventToUpdated.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isTrue();

        this.eventService.updateEvent(eventFromDB.get().getId(), eventToUpdated);

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

        verify(this.eventRepository, times(2)).save(any(Event.class));
        verify(this.eventRepository, times(3)).findOneById(any(Long.class));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUpdateEvent_WithNullBandShouldThrowException()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEventNullBand());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();
        eventToUpdated.setId(1L);

        when(this.eventRepository.save(eventToUpdated)).thenReturn(eventToUpdated);
        when(this.eventRepository.findOneById(eventToUpdated.getId())).thenReturn(Optional.of(eventToUpdated));
        final Optional<Event> eventFromDB = this.eventService.findEventById(eventToUpdated.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isTrue();

        this.eventService.updateEvent(eventFromDB.get().getId(), eventToUpdated);

        // Get Event Band List
        final List<Band> bands = MyEventUtils.convertSetToList(eventToUpdated.getBands());

        assertThat(eventToUpdated.getTitle()).isEqualTo(MyEventTestsUtils.EVENT_TITLE);
        assertThat(eventToUpdated.getNbStars()).isEqualTo(5);
        assertThat(eventToUpdated.getComment()).isEqualTo(MyEventTestsUtils.COMMENT);
        assertThat(eventToUpdated.getImgUrl()).containsSequence(MyEventTestsUtils.URL_PATTERN);
        assertThat(bands).isNotNull();
        assertThat(bands.size()).isEqualTo(0);
        assertThat(bands.get(0).getName()).isEqualTo(MyEventTestsUtils.BAND_NAME);

        verify(this.eventRepository, times(2)).save(any(Event.class));
        verify(this.eventRepository, times(3)).findOneById(any(Long.class));
    }

    @Test
    public void testUpdateEvent_WithNullMember()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEventNullMember());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();
        eventToUpdated.setId(1L);

        when(this.eventRepository.save(eventToUpdated)).thenReturn(eventToUpdated);
        when(this.eventRepository.findOneById(eventToUpdated.getId())).thenReturn(Optional.of(eventToUpdated));
        final Optional<Event> eventFromDB = this.eventService.findEventById(eventToUpdated.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isTrue();

        this.eventService.updateEvent(eventFromDB.get().getId(), eventToUpdated);

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

        verify(this.eventRepository, times(1)).save(any(Event.class));
        verify(this.eventRepository, times(2)).findOneById(any(Long.class));
    }

    @Test(expected = MyEventCustomException.class)
    public void testUpdateEvent_WithNullMemberShouldThrowException()
    {
        final Optional<Event> optional = Optional.ofNullable(MyEventTestsUtils.buildTestEventNullMember());

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();
        eventToUpdated.setId(1L);

        when(this.eventRepository.findOneById(eventToUpdated.getId())).thenReturn(Optional.of(eventToUpdated));
        final Optional<Event> eventFromDB = this.eventService.findEventById(eventToUpdated.getId());

        assertThat(eventFromDB).isNotNull();
        assertThat(eventFromDB.isPresent()).isTrue();

        this.eventService.updateEvent(eventFromDB.get().getId(), eventToUpdated);

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

        verify(this.eventRepository, times(1)).save(any(Event.class));
    }

    @Test(expected = MyEventCustomException.class)
    public void testUpdateEvent_ShouldThrowNPE_WithNullId()
    {
        final Event eventToUpdated = MyEventTestsUtils.buildTestEvent();
        this.eventService.updateEvent(null, eventToUpdated);

        assertThat(eventToUpdated).isNotNull();
        assertThat(eventToUpdated.getId()).isNotNull();
    }

    @Test(expected = MyEventCustomException.class)
    public void testUpdateEvent_ShouldThrowNPE_WithNull()
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
        assertThat(events.size()).isEqualTo(0);
    }
}

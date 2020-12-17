/*
 * ----------------------------------------------
 * Projet ou Module : tests-technique
 * Nom de la classe : EventControllerTest.java
 * Date de création : 17 déc. 2020
 * Heure de création : 11:23:58
 * Package : adeo.leroymerlin.cdp.controller
 * Auteur : Vincent Otchoun
 * Copyright © 2020 - All rights reserved.
 * ----------------------------------------------
 */
package adeo.leroymerlin.cdp.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.MyEventTestsUtils;
import adeo.leroymerlin.cdp.config.MyEventBaseConfig;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.service.EventService;

/**
 * Integration Tests Class for {@link EventController}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(value = { "classpath:application-test.yml" })
@ContextConfiguration(name = "eventControllerTest", classes = { MyEventBaseConfig.class, EventService.class, EventController.class })
@ActiveProfiles("test")
@Sql(scripts = { "classpath:scripts/schema.sql", "classpath:scripts/data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
// @Transactional
public class EventControllerTest
{
    //
    private static final String URL = "http://localhost:";// url of the REST server. This url can be that of a remote server.

    //
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventService eventService;

    @LocalServerPort
    private int port; // allows to use the local port of the server, otherwise a "Connection refused" error

    private Map<String, Event> map = new HashMap<>();

    private String getURLWithPort(String uri)
    {
        return URL + port + uri;
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.controller.EventController#findEvents()}.
     * 
     * @throws Exception
     */
    @Test
    public void testFindEvents() throws Exception
    {
        final ResponseEntity<Object> responseEntity = this.restTemplate.getForEntity(this.getURLWithPort("/api/events/"), Object.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        final List<Event> events = (List<Event>) responseEntity.getBody();
        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(5);
    }

    @Test
    public void testFindEvents_BadURLNotFound() throws Exception
    {
        final ResponseEntity<Object> responseEntity = this.restTemplate.getForEntity(this.getURLWithPort("/api/events565/api"), Object.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.controller.EventController#deleteEvent(java.lang.Long)}.
     */
    @Test
    public void testDeleteEvent()
    {
        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("id", MyEventTestsUtils.EXIST_ID);
        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{id}"), HttpMethod.DELETE, null, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteEvent_BadVariable()
    {
        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("id", MyEventTestsUtils.EXIST_ID);
        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{idBad}"), HttpMethod.DELETE, null, Void.class, variables);

        assertThat(responseEntity).isNull();
    }

    @Test
    public void testDeleteEvent_NotExist()
    {
        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("id", MyEventTestsUtils.NON_EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{id}"), HttpMethod.DELETE, null, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Test method for
     * {@link adeo.leroymerlin.cdp.controller.EventController#updateEvent(java.lang.Long, adeo.leroymerlin.cdp.model.Event)}.
     */
    @Test
    public void testUpdateEvent()
    {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of teh request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final HttpEntity<Event> requestEntity = new HttpEntity<Event>(MyEventTestsUtils.buildTestEvent(), headers);

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("id", MyEventTestsUtils.EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{id}"), HttpMethod.PUT, requestEntity, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Transactional
    public void testUpdateEvent_NullId_NotAllowed()
    {
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();
        this.map.put("EVENT", eventToUpdated);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of teh request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final HttpEntity<Event> requestEntity = new HttpEntity<Event>(eventToUpdated, headers);

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("id", null);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{id}"), HttpMethod.PUT, requestEntity, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @Test
    @Transactional
    public void testUpdateEvent_NullEvent_BadRequest()
    {
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        // final Event eventToUpdated = optional.get();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of teh request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final HttpEntity<Event> requestEntity = new HttpEntity<Event>(null, headers);

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("id", MyEventTestsUtils.EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{id}"), HttpMethod.PUT, requestEntity, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.controller.EventController#findEvents(java.lang.String)}.
     */
    @Test
    public void testFindEventsString()
    {
        // XXX: A METTRE A JOUR

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put("query", MyEventTestsUtils.EXIST_ID);

        //
        final ResponseEntity<Object> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/search/{query}"), HttpMethod.GET, null, Object.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        final List<Event> events = (List<Event>) responseEntity.getBody();
        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(5);
    }
}

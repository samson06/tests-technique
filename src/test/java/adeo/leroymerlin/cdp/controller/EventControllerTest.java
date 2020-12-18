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
public class EventControllerTest
{
    //
    private static final String URL = "http://localhost:";// url of the REST server. This url can be that of a remote server.
    private static final String ID_PARAM = "id";
    private static final String QUERY_PARAM = "query";
    private static final String WA_PATTERN = "Wa";
    private static final String LE_PATTERN = "le";
    private static final String ID_API_URL = "/api/events/{id}";
    private static final String QUERY_API_URL = "/api/events/search/{query}";
    private static final String ALL_API_URL = "/api/events/";

    //
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventService eventService;

    @LocalServerPort
    private int port; // allows to use the local port of the server, otherwise a "Connection refused" error

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
        final ResponseEntity<Object> responseEntity = this.restTemplate.getForEntity(this.getURLWithPort(ALL_API_URL), Object.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        @SuppressWarnings("unchecked")
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
        variables.put(ID_PARAM, MyEventTestsUtils.EXIST_ID);
        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort(ID_API_URL), HttpMethod.DELETE, null, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteEvent_BadVariable()
    {
        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(ID_PARAM, MyEventTestsUtils.EXIST_ID);
        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort("/api/events/{idBad}"), HttpMethod.DELETE, null, Void.class, variables);

        assertThat(responseEntity).isNull();
    }

    @Test
    public void testDeleteEvent_NotExist()
    {
        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(ID_PARAM, MyEventTestsUtils.NON_EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort(ID_API_URL), HttpMethod.DELETE, null, Void.class, variables);

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
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final HttpEntity<Event> requestEntity = new HttpEntity<Event>(MyEventTestsUtils.buildTestEvent(), headers);

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(ID_PARAM, MyEventTestsUtils.EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort(ID_API_URL), HttpMethod.PUT, requestEntity, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testUpdateEvent_NotExistId()
    {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(ID_PARAM, MyEventTestsUtils.NON_EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort(ID_API_URL), HttpMethod.PUT, null, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Transactional
    public void testUpdateEvent_NullId_NotAllowed()
    {
        final Optional<Event> optional = this.eventService.findEventById(MyEventTestsUtils.EXIST_ID);

        assertThat(optional).isNotNull();
        assertThat(optional.isPresent()).isTrue();

        final Event eventToUpdated = optional.get();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final HttpEntity<Event> requestEntity = new HttpEntity<Event>(eventToUpdated, headers);

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(ID_PARAM, null);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort(ID_API_URL), HttpMethod.PUT, requestEntity, Void.class, variables);

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

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // media type of the request body
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));// media type to response with

        final HttpEntity<Event> requestEntity = new HttpEntity<Event>(null, headers);

        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(ID_PARAM, MyEventTestsUtils.EXIST_ID);

        final ResponseEntity<Void> responseEntity = this.restTemplate.exchange(this.getURLWithPort(ID_API_URL), HttpMethod.PUT, requestEntity, Void.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test method for {@link adeo.leroymerlin.cdp.controller.EventController#findEvents(java.lang.String)}.
     */
    @Test
    public void testFindEventsString()
    {
        final Map<String, Long> variables = new HashMap<>(1);
        variables.put(QUERY_PARAM, MyEventTestsUtils.EXIST_ID);

        //
        final ResponseEntity<Object> responseEntity = this.restTemplate.exchange(this.getURLWithPort(QUERY_API_URL), HttpMethod.GET, null, Object.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        @SuppressWarnings("unchecked")
        final List<Event> events = (List<Event>) responseEntity.getBody();
        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(0);
    }

    @Test
    public void testFindEventsString_Query_Null()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(QUERY_PARAM, null);

        //
        final ResponseEntity<Object> responseEntity = this.restTemplate.exchange(this.getURLWithPort(QUERY_API_URL), HttpMethod.GET, null, Object.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());

        assertThat(responseEntity.getBody().toString()).isNotNull();
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testFindEventsString_Query_Empty()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(QUERY_PARAM, "");

        //
        final ResponseEntity<Object> responseEntity = this.restTemplate.exchange(this.getURLWithPort(QUERY_API_URL), HttpMethod.GET, null, Object.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());

        assertThat(responseEntity.getBody().toString()).isNotNull();
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    public void testFindEventsString_Query_Wa()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(QUERY_PARAM, WA_PATTERN);

        //
        final ResponseEntity<Object> responseEntity = this.restTemplate.exchange(this.getURLWithPort(QUERY_API_URL), HttpMethod.GET, null, Object.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        @SuppressWarnings("unchecked")
        final List<Event> events = (List<Event>) responseEntity.getBody();
        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(1);
    }

    @Test
    public void testFindEventsString_Query_le()
    {
        final Map<String, String> variables = new HashMap<>(1);
        variables.put(QUERY_PARAM, LE_PATTERN);

        //
        final ResponseEntity<Object> responseEntity = this.restTemplate.exchange(this.getURLWithPort(QUERY_API_URL), HttpMethod.GET, null, Object.class, variables);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());

        @SuppressWarnings("unchecked")
        final List<Event> events = (List<Event>) responseEntity.getBody();
        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(3);
    }
}

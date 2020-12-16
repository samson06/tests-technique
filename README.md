# My Event REST API

My Event is an application to manage musical events (proposed by _**ADEO Services**_ for _**technical tests** _).
My Event is simple RESTful web service using Spring and Java. This web service provides an in-memory musical events management service, with the capability to :
- Retrieve a list of musical events and their relationships in the system.
- Delete a musical events and their relationships in the system.
- Create or Update musical events information in the system.
- Retrieve a filtered list (one band has a member with the name matching the given pattern) of musical events in the system.

![](https://img.shields.io/badge/build-success-brightgreen.svg)

## Technical stack

![](https://img.shields.io/badge/Java_8-✓-blue.svg)
![](https://img.shields.io/badge/Maven-✓-blue.svg)
![](https://img.shields.io/badge/Spring_boot-✓-blue.svg)
![](https://img.shields.io/badge/Jpa-✓-blue.svg)
![](https://img.shields.io/badge/Hibernate-✓-blue.svg)
![](https://img.shields.io/badge/HSQLDB-✓-blue.svg)
![](https://img.shields.io/badge/Angular_JS-✓-blue.svg)

* This is a maven project.
* It uses HSQLDB as an in-memory database.
* It starts using this maven lifecycle ```mvn spring-boot:run``` or using the IDE
* The user interface is available at [http://localhost:8086]
* The API resources are available at [http://localhost:8086/api/] 

### Change configuration
Edit the configuration in the file [application.yml](/tests-technique/src/main/resources/application.yml)
```
server:
  port: 8086
  
spring:
  datasource:
    driverClassName: org.hsqldb.jdbc.JDBCDriver
    platform: hsql
    url: jdbc:hsqldb:mem:eventdb;DB_CLOSE_DELAY=-1 
    username: sa
    password: ""
  jpa:
    database: hsql
    showSql: true
    hibernate:
      ddlAuto: ""
    properties: 
      hibernate.format_sql: true #
```

## Context

* The user interface is tested and holds no identified issues. 
* We Identified a few things not working on the API.
* Your job is to fix the issues and add a new feature to the API.

## Identified Issues:

```
Please keep track (notes) of how you analysed and fixed the issues to help us 
understand the steps during the interview
```

1. Adding review does not work
2. Using the delete button works but elements comes back when i refresh the page 

## New Feature
```
Except for the testing libraries, No library/modules should be added to the dependencies
(use only pure java)
```

1. We would like to enable a new route for the API `/search/{query}`. It will allow us
to display filtered `events`.
The events are displayed only if at least one band has a member with the name matching the given
pattern.

Example: `/search/Wa`
```json
[{
    "title": "GrasPop Metal Meeting",
    "imgUrl": "img/1000.jpeg",
    "bands": [{
        "name": "Metallica",
        "members": [
            {
              "name": "Queen Anika Walsh"
            }
        ]
    },…
}…]
```

2. (BONUS) Add a `[count]` at each event and band 
to display the number of child items.

Example: `/search/Wa`
```json
[{
    "title": "GrasPop Metal Meeting [2]",
    "imgUrl": "img/1000.jpeg",
    "bands": [{
        "name": "Metallica [1]",
        "members": [
            {
              "name": "Queen Anika Walsh"
            }
        ]
    },…
}…]
```

## Team Appreciation

Team overall appreciation will be based on:
- Code readability, structure and consistency
- Tests, how they are written
- Bonus: usage of Functional concepts

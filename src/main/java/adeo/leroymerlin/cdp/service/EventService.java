package adeo.leroymerlin.cdp.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import adeo.leroymerlin.cdp.enumration.ErrorCodeEnum;
import adeo.leroymerlin.cdp.error.MyEventCustomException;
import adeo.leroymerlin.cdp.model.Band;
import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.repository.EventRepository;

@Service
public class EventService
{

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository)
    {
        this.eventRepository = eventRepository;
    }

    /**
     * Get all events and her relationships in the system.
     * 
     * @return list of all events in the system
     */
    public List<Event> getEvents()
    {
        return eventRepository.findAllBy();
    }

    /**
     * Delete event and her relationships from the system.
     * 
     * @param id musical event's Id.
     */
    public void delete(Long id)
    {
        eventRepository.delete(id);
    }

    /**
     * Create a new {@link Event} and her relationships in the system.
     * 
     * @param pEvent the musical event's to be created.
     * @return the created musical event's.
     */
    public Event createEvent(final Event pEvent)
    {
        // try to add new event record in yht system.
        try
        {
            final Event event = this.eventRepository.save(pEvent);
            Assert.notNull(event);
            return event;
        }
        catch (Exception e)
        {
            throw new MyEventCustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeEnum.CREATE_EVENT_ERROR.name(), e.getMessage());
        }
    }

    /**
     * Find musical event's and her relationships by Id.
     * 
     * @param pId the musical event's Id.
     * @return the musical event's object if found else return empty.
     */
    public Optional<Event> findEventById(final Long pId)
    {
        // try to find event by Id on the system.
        try
        {
            return Optional.of(this.eventRepository.findOneById(pId))//
            .filter(Objects::nonNull)//
            .filter(Optional::isPresent)//
            .map(Optional::get);//
        }
        catch (Exception e)
        {
            throw new MyEventCustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeEnum.FIND_EVENT_ERROR.name(), e.getMessage());
        }
    }

    /**
     * Update an existing event's information and her relationships in the system.
     * 
     * @param pId
     * @param pEvent the updated event's record.
     */
    public void updateEvent(final Long pId, final Event pEvent)
    {
        // try to update event by Id on the system.
        try
        {
            this.findEventById(pId).ifPresent(event -> {
                pEvent.setId(event.getId());
                this.createEvent(pEvent);
            });
        }
        catch (Exception e)
        {
            throw new MyEventCustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeEnum.UPDATE_EVENT_ERROR.name(), e.getMessage());
        }
    }

    /**
     * Get filtered event list with member name match given query.
     * 
     * @param query the given query pattern.
     * @return
     */
    public List<Event> getFilteredEvents(String query)
    {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here

        final List<Event> filteredList = events.stream()//
        .filter(event -> event.getBands().stream() // Get event Set<Band>
        .map(Band::getMembers) // Set<Member> stream get
        .filter(Objects::nonNull)// filter non null member for Set
        .findAny()// // Search
        .get()// // member Set get
        .stream()//
        .filter(Objects::nonNull)// filter non null member for Stream
        .anyMatch(member -> member.getName().contains(query))// member with the name matching the given pattern
        )//
        .collect(Collectors.toList());

        return filteredList;
    }
}

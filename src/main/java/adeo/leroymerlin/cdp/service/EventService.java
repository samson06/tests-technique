package adeo.leroymerlin.cdp.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adeo.leroymerlin.cdp.model.Event;
import adeo.leroymerlin.cdp.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Get all events and her relationships in the system.
     * 
     * @return list of all events in the system
     */
    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    /**
     * Delete event and her relationships from the system.
     * 
     * @param id musical event's Id.
     */
    public void delete(Long id) {
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
        return this.eventRepository.save(pEvent);
    }

    /**
     * Find musical event's and her relationships by Id.
     * 
     * @param pId the musical event's Id.
     * @return the musical event's object if found else return empty.
     */
    public Optional<Event> findEventById(final Long pId)
    {
        return Optional.of(this.eventRepository.findOneById(pId))//
        .filter(Objects::nonNull)//
        .filter(Optional::isPresent)//
        .map(Optional::get);//
    }

    /**
     * Update an existing event's information and her relationships in the system.
     * 
     * @param pId
     * @param pEvent the updated event's record.
     */
    public void updateEvent(final Long pId, final Event pEvent)
    {
        this.findEventById(pId).ifPresent(event -> {
            pEvent.setId(event.getId());
            this.createEvent(pEvent);
        });
    }

    /**
     * @param query
     * @return
     */
    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here

        return events;
    }
}

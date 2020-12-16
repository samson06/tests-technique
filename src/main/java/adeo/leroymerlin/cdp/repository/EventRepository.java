package adeo.leroymerlin.cdp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.model.Event;

@Transactional
@org.springframework.stereotype.Repository
public interface EventRepository extends Repository<Event, Long> {

    /**
     * Delete {@link Event} and her relationships from the system.
     * 
     * @param eventId musical event's identifier.
     */
    void delete(final Long eventId);

    /**
     * Get all events and her relationships in the system.
     * 
     * @return list of all events in the system.
     */
    List<Event> findAllBy();

    /**
     * Create a new {@link Event} and her relationships in the system.
     * 
     * @param pEvent the musical event's record to be created.
     * @return the created musical event's record.
     */
    Event save(final Event pEvent);

    /**
     * Find musical event's by its identifier.
     * 
     * @param pId the musical event's record Id.
     * @return the musical event's record if found else return empty.
     */
    Optional<Event> findOneById(final Long pId);
}

package adeo.leroymerlin.cdp.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.model.Event;

import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository extends Repository<Event, Long> {

    void delete(Long eventId);

    List<Event> findAllBy();
}
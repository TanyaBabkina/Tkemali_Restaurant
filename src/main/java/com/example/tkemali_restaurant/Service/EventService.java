package com.example.tkemali_restaurant.Service;

import com.example.tkemali_restaurant.models.Event;
import com.example.tkemali_restaurant.Repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }




    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Optional<Event> getEventByIdWithDetails(Long id) {
        return eventRepository.findByIdWithDetails(id);
    }
    public Page<Event> findAllPaginated(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findByIdWithDetails(id);
    }


    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

}

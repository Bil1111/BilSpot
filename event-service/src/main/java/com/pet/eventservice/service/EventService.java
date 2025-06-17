package com.pet.eventservice.service;

import com.pet.eventservice.dto.EventRequest;
import com.pet.eventservice.exception.ResourceNotFoundException;
import com.pet.eventservice.mapper.EventMapper;
import com.pet.eventservice.model.Event;
import com.pet.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Transactional
    public Event addEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(EventRequest request, Long id) {
        Event existingEvent = getEventById(id);
        eventMapper.updateEventFromDto(request, existingEvent);
        return eventRepository.save(existingEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}
package com.pet.eventservice.mapper;

import com.pet.eventservice.dto.EventRequest;
import com.pet.eventservice.dto.EventResponse;
import com.pet.eventservice.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEvent(EventRequest request);
    EventResponse toEventResponse(Event event);
    void updateEventFromDto(EventRequest dto, @MappingTarget Event event);
}
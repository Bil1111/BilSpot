package com.pet.eventservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.eventservice.dto.EventRequest;
import com.pet.eventservice.dto.EventResponse;
import com.pet.eventservice.exception.ResourceNotFoundException;
import com.pet.eventservice.mapper.EventMapper;
import com.pet.eventservice.model.Event;
import com.pet.eventservice.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EventService eventService() {
            return Mockito.mock(EventService.class);
        }

        @Bean
        public EventMapper eventMapper() {
            return Mockito.mock(EventMapper.class);
        }
    }

    private static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(10);

    // --- Create Events (POST) ---

    @Test
    @DisplayName("1.1: Create Event - Success (POST /v1/events)")
    void addEvent_Success() throws Exception {
        EventRequest eventRequest = new EventRequest("Atlas United", FUTURE_DATE, "Kyiv", "Okean Elzy", "Best festival", "url");
        Event savedEvent = new Event(1L, "Atlas United", FUTURE_DATE, "Kyiv", "Okean Elzy", "Best festival", "url");
        EventResponse eventResponse = new EventResponse(1L, "Atlas United", FUTURE_DATE, "Kyiv", "Okean Elzy", "Best festival", "url");

        when(eventService.addEvent(any(EventRequest.class))).thenReturn(savedEvent);
        when(eventMapper.toEventResponse(savedEvent)).thenReturn(eventResponse);

        mockMvc.perform(post("/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Atlas United")));
    }

    @Test
    @DisplayName("1.2: Create Event - Validation Error (POST /v1/events)")
    void addEvent_ValidationFails() throws Exception {
        EventRequest invalidRequest = new EventRequest("", FUTURE_DATE, "Venue", "Artist", "Valid description", "url");

        mockMvc.perform(post("/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // --- Read Events (GET) ---

    @Test
    @DisplayName("2.1: Get All Events - Success (GET /v1/events)")
    void getAllEvents_Success() throws Exception {
        Event event = new Event(1L, "Atlas United", FUTURE_DATE, "Kyiv", "Okean Elzy", "Best festival", "url");
        EventResponse eventResponse = new EventResponse(1L, "Atlas United", FUTURE_DATE, "Kyiv", "Okean Elzy", "Best festival", "url");

        when(eventService.getAllEvents()).thenReturn(List.of(event));
        when(eventMapper.toEventResponse(event)).thenReturn(eventResponse);

        mockMvc.perform(get("/v1/events"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Atlas United")));
    }

    @Test
    @DisplayName("2.2: Get Event by ID - Success (GET /v1/events/{id})")
    void getEventById_Success() throws Exception {
        Event event = new Event(1L, "Faine Misto", FUTURE_DATE, "Lviv", "Artist", "...", "url");
        EventResponse eventResponse = new EventResponse(1L, "Faine Misto", FUTURE_DATE, "Lviv", "Artist", "...", "url");

        when(eventService.getEventById(1L)).thenReturn(event);
        when(eventMapper.toEventResponse(event)).thenReturn(eventResponse);

        mockMvc.perform(get("/v1/events/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Faine Misto")));
    }

    @Test
    @DisplayName("2.3: Get Event by ID - Not Found (GET /v1/events/{id})")
    void getEventById_NotFound() throws Exception {
        String errorMessage = "Event not found with id: 99";
        when(eventService.getEventById(99L)).thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(get("/v1/events/99"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    // --- Update Events (PUT) ---

    @Test
    @DisplayName("3.1: Update Event - Success (PUT /v1/events/{id})")
    void updateEvent_Success() throws Exception {
        EventRequest eventRequest = new EventRequest("Updated Name", FUTURE_DATE, "Updated Venue", "Artist", "Оновлений Опис", "url");
        Event updatedEvent = new Event(1L, "Updated Name", FUTURE_DATE, "Updated Venue", "Artist", "Оновлений Опис", "url");
        EventResponse eventResponse = new EventResponse(1L, "Updated Name", FUTURE_DATE, "Updated Venue", "Artist", "...", "url");

        when(eventService.updateEvent(any(EventRequest.class), eq(1L))).thenReturn(updatedEvent);
        when(eventMapper.toEventResponse(updatedEvent)).thenReturn(eventResponse);

        mockMvc.perform(put("/v1/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    @DisplayName("3.2: Update Event - Not Found (PUT /v1/events/{id})")
    void updateEvent_NotFound() throws Exception {
        EventRequest eventRequest = new EventRequest("Any Name", FUTURE_DATE, "Any Venue", "Artist", "Опис, який чудово підходить", "url");
        String errorMessage = "Cannot update. Event not found with id: 99";
        when(eventService.updateEvent(any(EventRequest.class), eq(99L))).thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(put("/v1/events/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    // --- Delete Events (DELETE) ---

    @Test
    @DisplayName("4.1: Delete Event - Success (DELETE /v1/events/{id})")
    void deleteEvent_Success() throws Exception {
        doNothing().when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/v1/events/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("4.2: Delete Event - Not Found (DELETE /v1/events/{id})")
    void deleteEvent_NotFound() throws Exception {
        String errorMessage = "Cannot delete. Event not found with id: 99";
        doThrow(new ResourceNotFoundException(errorMessage)).when(eventService).deleteEvent(99L);

        mockMvc.perform(delete("/v1/events/99"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }
}
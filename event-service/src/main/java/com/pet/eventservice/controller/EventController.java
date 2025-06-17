package com.pet.eventservice.controller;

import com.pet.eventservice.dto.EventRequest;
import com.pet.eventservice.dto.EventResponse;
import com.pet.eventservice.mapper.EventMapper;
import com.pet.eventservice.model.Event;
import com.pet.eventservice.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/events") // Змінено на "events" для консистентності
@RequiredArgsConstructor
@Tag(name = "Event API", description = "API для керування подіями")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @Operation(summary = "Отримати список усіх подій", description = "Повертає повний список подій, наявних у системі.")
    @ApiResponse(responseCode = "200", description = "Успішно отримано список подій")
    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(eventMapper::toEventResponse)
                .toList();
    }

    @Operation(summary = "Отримати подію за ID", description = "Шукає та повертає подію за її унікальним ідентифікатором.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подію успішно знайдено"),
            @ApiResponse(responseCode = "404", description = "Подію з таким ID не знайдено", content = @Content)
    })
    @GetMapping("/{id}")
    public EventResponse getEventById(
            @Parameter(description = "Унікальний ID події", example = "1") @PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return eventMapper.toEventResponse(event);
    }

    @Operation(summary = "Додати нову подію", description = "Створює новий запис про подію в системі.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Подію успішно створено",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Некоректний запит (помилка валідації)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EventResponse> addEvent(
            @Valid @RequestBody EventRequest eventRequest) {
        Event savedEvent = eventService.addEvent(eventRequest);
        EventResponse response = eventMapper.toEventResponse(savedEvent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити існуючу подію", description = "Оновлює дані події за її ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подію успішно оновлено"),
            @ApiResponse(responseCode = "400", description = "Некоректний запит (помилка валідації)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Подію з таким ID не знайдено", content = @Content)
    })
    @PutMapping("/{id}")
    public EventResponse updateEvent(
            @Parameter(description = "Унікальний ID події для оновлення", example = "1") @PathVariable Long id,
            @Valid @RequestBody EventRequest eventRequest) {
        Event updatedEvent = eventService.updateEvent(eventRequest, id);
        return eventMapper.toEventResponse(updatedEvent);
    }

    @Operation(summary = "Видалити подію", description = "Видаляє подію із системи за її ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Подію успішно видалено"),
            @ApiResponse(responseCode = "404", description = "Подію з таким ID не знайдено", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(
            @Parameter(description = "Унікальний ID події для видалення", example = "1") @PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
package ru.practicum.explore.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventNewDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.EventUpdateRequestUser;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.dto.RequestUpdateDto;
import ru.practicum.explore.request.dto.RequestUpdateResultDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody EventNewDto eventNewDto) {
        EventFullDto addedEvent = eventService.saveEvent(userId, eventNewDto);
        log.info(String.format("Получен POST- запрос /users/{userId}/events на добавление события %s от пользователя: (id): %s", eventNewDto, userId));
        return addedEvent;
    }

    @GetMapping
    public List<EventShortDto> getEventsByInitiator(@PathVariable Long userId,
                                                    @RequestParam(name = "from", defaultValue = "0") int from,
                                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        List<EventShortDto> events = eventService.getAllEventsByInitiatorPrivate(userId, from, size);
        log.info(String.format("Получен GET- запрос  /users/{userId}/events?from={from}&size={size}, Просмотр списка событий от пользователя (id): %s с параметрами пагинации from: %s size: %s", userId, from, size));
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info(String.format("Получен GET- запрос: /users/{userId}/events/{eventId}. Просмотр события (id): %s от пользователя: (id): %s", eventId, userId));
        return eventService.getEventByIdPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody EventUpdateRequestUser eventUpdateRequestUser) {
        log.info(String.format("Получен PATCH- запрос: /users/{userId}/events/{eventsId} на обновление события (id): %s, от пользователя (id):  %s, обновленное событие (dto): %s", eventId, userId, eventUpdateRequestUser));
        return eventService.updateEventPrivate(userId, eventId, eventUpdateRequestUser);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getEventRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.info(String.format("Получен GET-запрос: /users/{userId}/events/{eventId}/requests, Просмотр запросов на участие в событии: (id): %s, созданном пользователем: (id): %s", eventId, userId));
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestUpdateResultDto updateStatusRequestsForEvent(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody RequestUpdateDto requestDto) {
        log.info(String.format("Получен PATCH-запрос /users/{userId}/events/{eventId}/requests. Обновление запроса на событие (id): %s, созданное пользователем (id): %s", eventId, userId));
        return eventService.updateStatusRequestsForEvent(userId, eventId, requestDto);
    }

}

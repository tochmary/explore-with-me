package ru.practicum.mainservice.user.controller.publicC;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.dto.EventShortDto;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/follows")
public class UserController {
    private final UserService userService;

    /**
     * Добавить подписку
     *
     * @param userId      id пользователя
     * @param followingId id подписки (на которого подписывается текущий пользователь)
     */
    @PatchMapping("/{followingId}")
    public void addFollowing(@PathVariable long userId,
                             @PathVariable long followingId) {
        log.info("Подписаться пользователю с userId={} на пользователя с userId={}", userId, followingId);
        userService.addFollowing(userId, followingId);
    }

    /**
     * Отмена подписки
     *
     * @param userId      id пользователя
     * @param followingId id подписки (на которого отподписывается текущий пользователь)
     */
    @DeleteMapping("/{followingId}")
    public void removeFollowing(@PathVariable long userId,
                                @PathVariable long followingId) {
        log.info("Отменить подписку пользователю с userId={} на пользователя с userId={}", userId, followingId);
        userService.removeFollowing(userId, followingId);
    }

    /**
     * Получение списка актуальных событий, опубликованных пользователями,
     * на которых подписан текущий пользователь.
     *
     * @param userId id пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return List<UserDto> список пользователей
     */
    @GetMapping("/events")
    public List<EventShortDto> getEventsFollows(@PathVariable long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка актуальных событий, опубликованных пользователями, " +
                "на которых подписан текущий пользователь с userId={} (from={}, size={})", userId, from, size);
        List<Event> eventList = userService.getEventsFollows(userId, from, size);
        return EventMapper.getEventShortDtoList(eventList);
    }
}
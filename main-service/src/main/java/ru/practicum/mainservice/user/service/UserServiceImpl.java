package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.common.exception.NotFoundException;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.user.model.entity.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.common.Utility.checkForNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventService eventService;
    private final EntityManager entityManager;

    @Override
    public List<User> getUsers(Integer from, Integer size) {
        log.debug("Получение списка всех пользователей c from={}, size={}", from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return userRepository.findAll(pr).toList();
    }

    @Override
    public List<User> getUsers(List<Long> userIds, Integer from, Integer size) {
        log.debug("Получение списка всех пользователей c from={}, size={}", from, size);
        PageRequest pr = PageRequest.of(from / size, size);
        return userRepository.findUsersByIdIn(userIds, pr).toList();
    }

    @Override
    @Transactional
    public User addUser(User user) {
        log.debug("Добавление пользователя {}", user);
        checkForNull(user, "user");
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.debug("Удаление пользователя с userId={}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void checkUser(long userId) {
        log.debug("Проверка существования пользователя с userId={}", userId);
        getUserByUserId(userId);
    }

    @Override
    public User getUserByUserId(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователя с userId=" + userId + " не существует!")
        );
    }

    @Override
    public User getUser(long userId) {
        log.debug("Получение пользователя");
        return getUserByUserId(userId);
    }

    @Override
    @Transactional
    public void addFollowing(long userId, long followingId) {
        log.debug("Подписаться пользователю с userId={} на пользователя с userId={}", userId, followingId);
        User user = getUserByUserId(userId);
        User following = getUserByUserId(followingId);
        user.addFollowing(following);
        entityManager.detach(user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeFollowing(long userId, long followingId) {
        log.debug("Отменить подписку пользователю с userId={} на пользователя с userId={}", userId, followingId);
        User user = getUserByUserId(userId);
        User following = getUserByUserId(followingId);
        user.deleteFollowing(following);
        entityManager.detach(user);
        userRepository.save(user);
    }

    @Override
    public List<Event> getEventsFollows(long userId, Integer from, Integer size) {
        log.debug("Получение списка актуальных событий, опубликованных пользователями, " +
                "на которых подписан текущий пользователь с userId={} (from={}, size={})", userId, from, size);
        User user = getUserByUserId(userId);
        List<Long> followingIdList = user.getFollowings().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        return eventService.getEventsByUsers(followingIdList, from, size);
    }
}

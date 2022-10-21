package ru.practicum.mainservice.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.user.model.entity.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository mockUserRepository;
    @Mock
    EventService mockEventService;
    @Mock
    EntityManager mockEntityManager;

    UserServiceImpl userServiceImpl;

    private static final User USER_1 = new User(1L, "Maria", "maria_smart@mail.ru", new ArrayList<>());
    private static final User USER_2 = new User(2L, "Ivan", "ivan_humble@mail.ru", new ArrayList<>());

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl(mockUserRepository, mockEventService, mockEntityManager);
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void getUsers() {
        List<User> sourceUsers = List.of(USER_1, USER_2);
        Mockito.when(mockUserRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(sourceUsers));

        List<User> targetUsers = userServiceImpl.getUsers(0, 20);
        Assertions.assertEquals(sourceUsers.size(), targetUsers.size());
        for (User sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    @DisplayName("Добавление подписки")
    void addFollowing() {
        Long userId1 = USER_1.getId();
        Long userId2 = USER_2.getId();

        Mockito
                .when(mockUserRepository.findById(userId1))
                .thenReturn(Optional.of(USER_1));
        Mockito
                .when(mockUserRepository.findById(userId2))
                .thenReturn(Optional.of(USER_2));

        userServiceImpl.addFollowing(userId1, userId2);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .save(any(User.class));
    }

    @Test
    @DisplayName("Отмена подписки")
    void removeFollowing() {
        Long userId1 = USER_1.getId();
        Long userId2 = USER_2.getId();

        Mockito
                .when(mockUserRepository.findById(userId1))
                .thenReturn(Optional.of(USER_1));
        Mockito
                .when(mockUserRepository.findById(userId2))
                .thenReturn(Optional.of(USER_2));

        userServiceImpl.removeFollowing(userId1, userId2);

        Mockito
                .verify(mockUserRepository, Mockito.times(1))
                .save(any(User.class));
    }

    @Test
    @DisplayName("Получение событий подписок")
    void getEventsFollows() {
        User USER_3 = new User(3L, "Daris", "daria_super@mail.ru", List.of(USER_1, USER_2));
        Long userId3 = USER_3.getId();
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event1.setId(2L);
        List<Event> sourceEvents = List.of(event1, event2);
        Mockito.when(mockEventService.getEventsByUsers(any(), any(), any())).thenReturn(sourceEvents);

        Mockito
                .when(mockUserRepository.findById(userId3))
                .thenReturn(Optional.of(USER_3));

        List<Event> targetEvents = userServiceImpl.getEventsFollows(userId3, 0, 20);
        Assertions.assertEquals(sourceEvents.size(), targetEvents.size());
        for (Event sourceEvent : sourceEvents) {
            assertThat(targetEvents, hasItem(allOf(
                    hasProperty("id", notNullValue())
            )));
        }
    }
}
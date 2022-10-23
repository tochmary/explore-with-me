package ru.practicum.mainservice.user.controller.publicC;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.category.model.entity.Category;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.dto.EventShortDto;
import ru.practicum.mainservice.event.model.entity.Event;
import ru.practicum.mainservice.user.model.entity.User;
import ru.practicum.mainservice.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class)
class FollowControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Получение списка актуальных событий подписок")
    void getEventsFollows() throws Exception {
        User user1 = new User(1L, "Maria", "maria_smart@mail.ru", new ArrayList<>());
        User user2 = new User(2L, "Ivan", "ivan_humble@mail.ru", new ArrayList<>());
        User user3 = new User(3L, "Daria", "daria_funny@mail.ru", new ArrayList<>());
        Event event1 = new Event();
        event1.setId(1L);
        event1.setAnnotation("annotation1");
        event1.setCategory(new Category(1L, "cat1"));
        event1.setInitiator(user2);
        Event event2 = new Event();
        event2.setId(2L);
        event2.setAnnotation("annotation2");
        event2.setCategory(new Category(2L, "cat2"));
        event2.setInitiator(user3);
        List<Event> eventList = List.of(event1, event2);
        when(userService.getEventsFollows(user1.getId(), 0, 20))
                .thenReturn(eventList);

        List<EventShortDto> eventShortDtoList = EventMapper.getEventShortDtoList(eventList);
        mvc.perform(get("/users/{userId}/follows/events", user1.getId())
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(eventShortDtoList)));
    }
}
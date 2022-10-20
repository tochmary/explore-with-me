package ru.practicum.mainservice.user.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.dto.NewUserRequest;
import ru.practicum.mainservice.user.model.dto.UserDto;
import ru.practicum.mainservice.user.model.entity.User;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService userService;

    /**
     * Получение информации о пользователях
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы)
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return List<UserDto> список пользователей
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        List<User> userList;
        if (ids.isEmpty()) {
            log.info("Получение списка пользователей");
            userList = userService.getUsers(from, size);
        } else {
            log.info("Получение информации пользователя с id={}", ids);
            userList = userService.getUsers(ids, from, size);
        }
        return UserMapper.getUserDtoList(userList);
    }

    /**
     * Добавление нового пользователя
     *
     * @param userDto Данные нового пользователя
     * @return UserDto Пользователь
     */
    @PostMapping
    public UserDto registerUser(@RequestBody NewUserRequest userDto) {
        log.info("Добавление пользователя {}", userDto);
        User user = UserMapper.toUser(userDto);
        user = userService.addUser(user);
        return UserMapper.toUserDto(user);
    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с userId={}", userId);
        userService.deleteUser(userId);
    }
}
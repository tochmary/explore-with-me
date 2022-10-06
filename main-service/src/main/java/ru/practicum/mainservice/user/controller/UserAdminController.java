package ru.practicum.mainservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.dto.NewUserRequest;
import ru.practicum.mainservice.user.model.dto.UserDto;
import ru.practicum.mainservice.user.model.entity.User;
import ru.practicum.mainservice.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Получение списка пользователей");
        List<User> userList = userService.getUsers();
        return UserMapper.getUserDtoList(userList);
    }

    @PostMapping
    public UserDto addUser(@RequestBody NewUserRequest userDto) {
        log.info("Добавление пользователя {}", userDto);
        User user = UserMapper.toUser(userDto);
        user = userService.addUser(user);
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с userId={}", userId);
        userService.deleteUser(userId);
    }
}
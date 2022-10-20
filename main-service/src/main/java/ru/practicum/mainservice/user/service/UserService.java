package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers(Integer from, Integer size);

    List<User> getUsers(List<Long> userIds, Integer from, Integer size);

    User addUser(User user);

    void deleteUser(long userId);

    void checkUser(long userId);

    User getUserByUserId(long userId);

    User getUser(long userId);
}

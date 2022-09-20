package ru.practicum.mainservice.user.service;

import ru.practicum.mainservice.user.model.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User addUser(User user);

    void deleteUser(long userId);

    void checkUser(long userId);
}

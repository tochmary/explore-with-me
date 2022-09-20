package ru.practicum.mainservice.user.mapper;

import ru.practicum.mainservice.user.model.dto.UserDto;
import ru.practicum.mainservice.user.model.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static List<UserDto> getUserDtoList(List<User> userList) {
        return userList.stream()
                .map(UserMapper::toUserDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
    }
}
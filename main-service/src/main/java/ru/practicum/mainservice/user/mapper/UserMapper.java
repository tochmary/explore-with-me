package ru.practicum.mainservice.user.mapper;

import ru.practicum.mainservice.user.model.dto.NewUserRequest;
import ru.practicum.mainservice.user.model.dto.UserDto;
import ru.practicum.mainservice.user.model.dto.UserShortDto;
import ru.practicum.mainservice.user.model.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.common.Utility.checkForNull;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        checkForNull(user, "user");
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        if (user.getFollowings() != null) {
            userDto.setFollowings(getUserShortDtoList(user.getFollowings()));
        }
        return userDto;
    }

    public static User toUser(NewUserRequest userDto) {
        checkForNull(userDto, "userDto");
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static List<UserDto> getUserDtoList(List<User> userList) {
        return userList.stream()
                .map(UserMapper::toUserDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
    }

    public static UserShortDto toUserShortDto(User user) {
        checkForNull(user, "user");
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static List<UserShortDto> getUserShortDtoList(List<User> userList) {
        return userList.stream()
                .map(UserMapper::toUserShortDto)
                .sorted(Comparator.comparing(UserShortDto::getId))
                .collect(Collectors.toList());
    }
}
package ru.practicum.users;

import org.mapstruct.Mapper;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User fromDto(UserDto userDto);
}

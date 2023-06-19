package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.core.pagination.PaginationService;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.DataNotFoundException;
import ru.practicum.users.UserMapper;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PaginationService paginationService;

    private UserDto prepareDto(User user) {
        return userMapper.toDto(user);
    }

    private User prepareDao(UserDto userDto) {
        return userMapper.fromDto(userDto);
    }

    private User findById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException(User.class.getName(), userId)
                );
    }

    private void checkName(String name) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            throw new ConflictException(String.format("User name '%s' already exist", name));
        }
    }

    public List<UserDto> findAllByUser(List<Integer> ids, Integer from, Integer size) {
        List<User> result;

        if (ids == null || ids.isEmpty()) {
            result = userRepository.findAll(paginationService.getPageable(from, size)).getContent();
        } else {
            result = userRepository.findAllByIdIn(ids, paginationService.getPageable(from, size));
        }

        return result.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto save(UserDto userDto) {
        checkName(userDto.getName());
        User newUser = userRepository.save(prepareDao(userDto));

        return prepareDto(newUser);
    }

    public void delete(int userId) {
        User deletedUser = findById(userId);
        userRepository.delete(deletedUser);
    }

    public User checkUserById(int id) {
        return findById(id);
    }
}

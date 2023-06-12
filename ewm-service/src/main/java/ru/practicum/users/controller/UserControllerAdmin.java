package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserControllerAdmin {
    private final UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserDto>> getAll(
            @RequestParam(required = false) List<Integer> ids,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        List<UserDto> result = userService.findAllByUser(ids, from, size);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<UserDto> save(@Valid @RequestBody UserDto userDto) {
        UserDto newUser = userService.save(userDto);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<String> delete(@PathVariable int userId) {
        userService.delete(userId);

        return new ResponseEntity<>("Пользователь удален", HttpStatus.NO_CONTENT);
    }
}

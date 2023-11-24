package com.taskmanager.controller;

import com.taskmanager.domain.model.DataTablesResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.dto.user.CreateUserDto;
import com.taskmanager.domain.dto.user.TableUserDto;
import com.taskmanager.domain.dto.user.UpdateUserDto;
import com.taskmanager.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @ResponseStatus(OK)
    public GenericResponse<Void> createUser(@Valid @RequestBody CreateUserDto createUserDTO, Principal principal){
        return userService.create(createUserDTO, principal);
    }

    @GetMapping("/users")
    @ResponseStatus(OK)
    public GenericResponse<DataTablesResponse<TableUserDto>> findAll(
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        return userService.findAll(pageNumber, pageSize, sort);
    }

    @DeleteMapping("/user/{userId}")
    @ResponseStatus(OK)
    public GenericResponse<Void> deleteUser(@PathVariable Long userId, Principal principal){
        return userService.delete(userId, principal);
    }

    @PutMapping("/user/{userId}")
    @ResponseStatus(OK)
    public GenericResponse<Void> updateUser(
            @PathVariable Long userId, @Valid @RequestBody UpdateUserDto userDto, Principal principal){
        return userService.update(userId, userDto, principal);
    }
}

package com.taskmanager.domain.dto.user;

import com.taskmanager.util.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @Email
    private String email;
    private String username;
    private Set<Role> roles;
}

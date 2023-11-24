package com.taskmanager.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableUserDto {

    private Long id;
    private String email;
    private String username;
    private Set<String> roles;
    private String createDate;
    private String lastModifiedDate;
}

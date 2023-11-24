package com.taskmanager.domain.mapper;


import com.taskmanager.domain.dto.user.CreateUserDto;
import com.taskmanager.domain.dto.user.TableUserDto;
import com.taskmanager.domain.dto.user.UpdateUserDto;
import com.taskmanager.domain.entity.RoleEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.util.DateUtil;
import com.taskmanager.util.Role;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public TableUserDto mapTableUserDto(UserEntity user){

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return TableUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(roles)
                .createDate(DateUtil.convertLocalDateTimeToString(user.getCreatedDate()))
                .lastModifiedDate(DateUtil.convertLocalDateTimeToString(user.getLastModifiedDate()))
                .build();
    }

    public UserEntity mapToUserEntity(UserEntity userEntity, UpdateUserDto userDto, UserEntity userAuthenticated){
        Set<RoleEntity> roles = mapSetRoleEntity(userDto.getRoles());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setRoles(roles);
        userEntity.setLastModifiedBy(userAuthenticated.getId());
        userEntity.setLastModifiedDate(LocalDateTime.now());
        return userEntity;
    }

    public UserEntity mapToUserEntity(CreateUserDto userDto, UserEntity userAuthenticated){
        Set<RoleEntity> roles = mapSetRoleEntity(userDto.getRoles());
        return UserEntity.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .roles(roles)
                .createdBy(userAuthenticated.getId())
                .build();
    }

    private Set<RoleEntity> mapSetRoleEntity(Set<Role> roles){
        return roles
                .stream()
                .map(role -> RoleEntity.builder()
                        .id(role.getId())
                        .name(role)
                        .build())
                .collect(Collectors.toSet());
    }
}

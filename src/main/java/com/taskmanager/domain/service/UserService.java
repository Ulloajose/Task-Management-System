package com.taskmanager.domain.service;

import com.taskmanager.domain.dto.user.CreateUserDto;
import com.taskmanager.domain.dto.user.TableUserDto;
import com.taskmanager.domain.dto.user.UpdateUserDto;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.domain.exeption.NotFoundException;
import com.taskmanager.domain.exeption.UserExistsException;
import com.taskmanager.domain.mapper.GenericResponseMapper;
import com.taskmanager.domain.mapper.UserMapper;
import com.taskmanager.domain.model.DataTablesResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.repository.UserRepository;
import com.taskmanager.util.Constant;
import com.taskmanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = findByUsername(username);
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(Constant.USER_NOT_FOUND));
    }

    public UserEntity findByIdActive(Long id){
        return userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new UsernameNotFoundException(Constant.USER_NOT_FOUND));
    }

    public GenericResponse<Void> create(CreateUserDto user, Principal principal){
        UserEntity userAuthenticated = findByUsername(principal.getName());
        Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (optionalUser.isPresent()){
           throw new UserExistsException(Constant.USER_EXIST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserEntity userEntity = UserMapper.mapToUserEntity(user, userAuthenticated);
        userRepository.save(userEntity);
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.CHANGES_SUCCESS);
    }

    public GenericResponse<Void> delete(Long id, Principal principal){
        UserEntity userAuthenticated = findByUsername(principal.getName());
        UserEntity userEntity = findByIdActive(id);
        userEntity.setDeleted(true);
        userEntity.setLastModifiedBy(userAuthenticated.getId());
        userEntity.setLastModifiedDate(LocalDateTime.now());
        userRepository.save(userEntity);
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.USER_DELETED_SUCCESS);
    }

    public GenericResponse<Void> update(Long id, UpdateUserDto userDto, Principal principal){
        UserEntity userAuthenticated = findByUsername(principal.getName());
        UserEntity userEntity = findByIdActive(id);

        if(!Objects.equals(userEntity.getEmail(), userDto.getEmail())
                || !Objects.equals(userEntity.getUsername(), userDto.getUsername())){
            Optional<UserEntity> optionalUser = userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getEmail());
            if (optionalUser.isPresent()){
                throw new UserExistsException(Constant.USER_EXIST);
            }
        }

       userEntity = UserMapper.mapToUserEntity(userEntity, userDto, userAuthenticated);
        userRepository.save(userEntity);
        return GenericResponseMapper.buildGenericResponse(null, HttpStatus.OK, Constant.USER_UPDATE_SUCCESS);
    }

    public GenericResponse<DataTablesResponse<TableUserDto>> findAll(
            int pageNumber, int pageSize, String[] sort) throws NotFoundException {

        List<Sort.Order> orders = PaginationUtil.setSort(sort);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orders));
        Page<UserEntity> userEntityPage = userRepository.findAllByDeletedFalse(pageable);
        List<TableUserDto> bList = userEntityPage
                .getContent()
                .stream()
                .map(UserMapper::mapTableUserDto)
                .toList();

        DataTablesResponse<TableUserDto> tablesResponse = new DataTablesResponse<>();
        tablesResponse.setData(bList);
        tablesResponse.setCurrentPage(userEntityPage.getNumber());
        tablesResponse.setTotalItems(userEntityPage.getTotalElements());
        tablesResponse.setTotalPages(userEntityPage.getTotalPages());
        return GenericResponseMapper.buildGenericResponse(tablesResponse, HttpStatus.OK, HttpStatus.OK.toString());
    }
}

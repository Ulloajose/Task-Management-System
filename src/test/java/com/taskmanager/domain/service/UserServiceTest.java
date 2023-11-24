package com.taskmanager.domain.service;

import com.sun.security.auth.UserPrincipal;
import com.taskmanager.domain.dto.user.CreateUserDto;
import com.taskmanager.domain.dto.user.TableUserDto;
import com.taskmanager.domain.dto.user.UpdateUserDto;
import com.taskmanager.domain.entity.RoleEntity;
import com.taskmanager.domain.entity.UserEntity;
import com.taskmanager.domain.exeption.NotFoundException;
import com.taskmanager.domain.exeption.UserExistsException;
import com.taskmanager.domain.model.DataTablesResponse;
import com.taskmanager.domain.model.DetailResponse;
import com.taskmanager.domain.model.GenericResponse;
import com.taskmanager.domain.model.ResultResponse;
import com.taskmanager.domain.repository.UserRepository;
import com.taskmanager.util.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        HashSet<RoleEntity> roles = new HashSet<>();
        userEntity.setRoles(roles);
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = userService.loadUserByUsername("janedoe");
        verify(userRepository).findByUsername(Mockito.<String>any());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonLocked());
        assertTrue(actualLoadUserByUsernameResult.isCredentialsNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
        assertEquals(roles, actualLoadUserByUsernameResult.getAuthorities());
    }

    @Test
    void testLoadUserByUsername5() throws UsernameNotFoundException {
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#findByUsername(String)}
     */
    @Test
    void testFindByUsername() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UserEntity actualFindByUsernameResult = userService.findByUsername("janedoe");
        verify(userRepository).findByUsername(Mockito.<String>any());
        assertSame(userEntity, actualFindByUsernameResult);
    }

    /**
     * Method under test: {@link UserService#findByUsername(String)}
     */
    @Test
    void testFindByUsername2() {
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("janedoe"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#findByUsername(String)}
     */
    @Test
    void testFindByUsername3() {
        when(userRepository.findByUsername(Mockito.<String>any())).thenThrow(new UserExistsException("An error occurred"));
        assertThrows(UserExistsException.class, () -> userService.findByUsername("janedoe"));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#findByIdActive(Long)}
     */
    @Test
    void testFindByIdActive() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult);
        UserEntity actualFindByIdActiveResult = userService.findByIdActive(1L);
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        assertSame(userEntity, actualFindByIdActiveResult);
    }

    /**
     * Method under test: {@link UserService#findByIdActive(Long)}
     */
    @Test
    void testFindByIdActive2() {
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(UsernameNotFoundException.class, () -> userService.findByIdActive(1L));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#findByIdActive(Long)}
     */
    @Test
    void testFindByIdActive3() {
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any()))
                .thenThrow(new UserExistsException("An error occurred"));
        assertThrows(UserExistsException.class, () -> userService.findByIdActive(1L));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#create(CreateUserDto, Principal)}
     */
    @Test
    void testCreate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        CreateUserDto user = new CreateUserDto();
        assertThrows(UserExistsException.class, () -> userService.create(user, new UserPrincipal("principal")));
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#create(CreateUserDto, Principal)}
     */
    @Test
    void testCreate2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new UserExistsException("An error occurred"));
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        CreateUserDto user = new CreateUserDto();
        assertThrows(UserExistsException.class, () -> userService.create(user, new UserPrincipal("principal")));
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#create(CreateUserDto, Principal)}
     */
    @Test
    void testCreate3() {
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(emptyResult);
        CreateUserDto user = new CreateUserDto();
        assertThrows(UsernameNotFoundException.class, () -> userService.create(user, new UserPrincipal("principal")));
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#delete(Long, Principal)}
     */
    @Test
    void testDelete() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setCreatedBy(1L);
        userEntity3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setDeleted(true);
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setId(1L);
        userEntity3.setLastModifiedBy(1L);
        userEntity3.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setPassword("iloveyou");
        userEntity3.setRoles(new HashSet<>());
        userEntity3.setUsername("janedoe");
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity3);
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        GenericResponse<Void> actualDeleteResult = userService.delete(1L, new UserPrincipal("principal"));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).save(Mockito.<UserEntity>any());
        ResultResponse result = actualDeleteResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        assertEquals("The user have been deleted successfully!", getResult.getDetail());
        assertNull(actualDeleteResult.getData());
        assertEquals(1, details.size());
    }

    /**
     * Method under test: {@link UserService#delete(Long, Principal)}
     */
    @Test
    void testDelete2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);
        when(userRepository.save(Mockito.<UserEntity>any())).thenThrow(new UserExistsException("An error occurred"));
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(UserExistsException.class, () -> userService.delete(1L, new UserPrincipal("principal")));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).save(Mockito.<UserEntity>any());
    }

    /**
     * Method under test: {@link UserService#delete(Long, Principal)}
     */
    @Test
    void testDelete3() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(emptyResult);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(UsernameNotFoundException.class, () -> userService.delete(1L, new UserPrincipal("principal")));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#delete(Long, Principal)}
     */
    @Test
    void testDelete4() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getId()).thenReturn(1L);
        doNothing().when(userEntity).setCreatedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setDeleted(anyBoolean());
        doNothing().when(userEntity).setLastModifiedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setEmail(Mockito.<String>any());
        doNothing().when(userEntity).setId(Mockito.<Long>any());
        doNothing().when(userEntity).setPassword(Mockito.<String>any());
        doNothing().when(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        doNothing().when(userEntity).setUsername(Mockito.<String>any());
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setCreatedBy(1L);
        userEntity3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setDeleted(true);
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setId(1L);
        userEntity3.setLastModifiedBy(1L);
        userEntity3.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setPassword("iloveyou");
        userEntity3.setRoles(new HashSet<>());
        userEntity3.setUsername("janedoe");
        when(userRepository.save(Mockito.<UserEntity>any())).thenReturn(userEntity3);
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        GenericResponse<Void> actualDeleteResult = userService.delete(1L, new UserPrincipal("principal"));
        verify(userEntity).setCreatedBy(Mockito.<Long>any());
        verify(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).setDeleted(anyBoolean());
        verify(userEntity).setLastModifiedBy(Mockito.<Long>any());
        verify(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).getId();
        verify(userEntity).setEmail(Mockito.<String>any());
        verify(userEntity).setId(Mockito.<Long>any());
        verify(userEntity).setPassword(Mockito.<String>any());
        verify(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        verify(userEntity).setUsername(Mockito.<String>any());
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).save(Mockito.<UserEntity>any());
        ResultResponse result = actualDeleteResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        assertEquals("The user have been deleted successfully!", getResult.getDetail());
        assertNull(actualDeleteResult.getData());
        assertEquals(1, details.size());
    }

    /**
     * Method under test: {@link UserService#update(Long, UpdateUserDto, Principal)}
     */
    @Test
    void testUpdate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setCreatedBy(1L);
        userEntity3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setDeleted(true);
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setId(1L);
        userEntity3.setLastModifiedBy(1L);
        userEntity3.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setPassword("iloveyou");
        userEntity3.setRoles(new HashSet<>());
        userEntity3.setUsername("janedoe");
        Optional<UserEntity> ofResult3 = Optional.of(userEntity3);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(ofResult3);
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UpdateUserDto userDto = new UpdateUserDto();
        assertThrows(UserExistsException.class, () -> userService.update(1L, userDto, new UserPrincipal("principal")));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#update(Long, UpdateUserDto, Principal)}
     */
    @Test
    void testUpdate2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new UserExistsException("An error occurred"));
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UpdateUserDto userDto = new UpdateUserDto();
        assertThrows(UserExistsException.class, () -> userService.update(1L, userDto, new UserPrincipal("principal")));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#update(Long, UpdateUserDto, Principal)}
     */
    @Test
    void testUpdate3() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        UserEntity userEntity2 = mock(UserEntity.class);
        when(userEntity2.getEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(userEntity2).setCreatedBy(Mockito.<Long>any());
        doNothing().when(userEntity2).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity2).setDeleted(anyBoolean());
        doNothing().when(userEntity2).setLastModifiedBy(Mockito.<Long>any());
        doNothing().when(userEntity2).setLastModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity2).setEmail(Mockito.<String>any());
        doNothing().when(userEntity2).setId(Mockito.<Long>any());
        doNothing().when(userEntity2).setPassword(Mockito.<String>any());
        doNothing().when(userEntity2).setRoles(Mockito.<Set<RoleEntity>>any());
        doNothing().when(userEntity2).setUsername(Mockito.<String>any());
        userEntity2.setCreatedBy(1L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(true);
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setId(1L);
        userEntity2.setLastModifiedBy(1L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword("iloveyou");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername("janedoe");
        Optional<UserEntity> ofResult2 = Optional.of(userEntity2);

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setCreatedBy(1L);
        userEntity3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setDeleted(true);
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setId(1L);
        userEntity3.setLastModifiedBy(1L);
        userEntity3.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity3.setPassword("iloveyou");
        userEntity3.setRoles(new HashSet<>());
        userEntity3.setUsername("janedoe");
        Optional<UserEntity> ofResult3 = Optional.of(userEntity3);
        when(userRepository.findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(ofResult3);
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(ofResult2);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UpdateUserDto userDto = new UpdateUserDto();
        assertThrows(UserExistsException.class, () -> userService.update(1L, userDto, new UserPrincipal("principal")));
        verify(userEntity2).setCreatedBy(Mockito.<Long>any());
        verify(userEntity2).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(userEntity2).setDeleted(anyBoolean());
        verify(userEntity2).setLastModifiedBy(Mockito.<Long>any());
        verify(userEntity2).setLastModifiedDate(Mockito.<LocalDateTime>any());
        verify(userEntity2).getEmail();
        verify(userEntity2).setEmail(Mockito.<String>any());
        verify(userEntity2).setId(Mockito.<Long>any());
        verify(userEntity2).setPassword(Mockito.<String>any());
        verify(userEntity2).setRoles(Mockito.<Set<RoleEntity>>any());
        verify(userEntity2).setUsername(Mockito.<String>any());
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
        verify(userRepository).findByUsernameOrEmail(Mockito.<String>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#update(Long, UpdateUserDto, Principal)}
     */
    @Test
    void testUpdate4() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        Optional<UserEntity> emptyResult = Optional.empty();
        when(userRepository.findByIdAndDeletedFalse(Mockito.<Long>any())).thenReturn(emptyResult);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        UpdateUserDto userDto = new UpdateUserDto();
        assertThrows(UsernameNotFoundException.class,
                () -> userService.update(1L, userDto, new UserPrincipal("principal")));
        verify(userRepository).findByIdAndDeletedFalse(Mockito.<Long>any());
        verify(userRepository).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll() throws NotFoundException {
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "ROLE_"});
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(0L, data.getTotalItems());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, details.size());
        assertTrue(data.getData().isEmpty());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll2() throws NotFoundException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");

        ArrayList<UserEntity> content = new ArrayList<>();
        content.add(userEntity);
        PageImpl<UserEntity> pageImpl = new PageImpl<>(content);
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(pageImpl);
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "ROLE_"});
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, data.getData().size());
        assertEquals(1, details.size());
        assertEquals(1L, data.getTotalItems());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll3() throws NotFoundException {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setCreatedBy(0L);
        userEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setDeleted(false);
        userEntity2.setEmail("john.smith@example.org");
        userEntity2.setId(2L);
        userEntity2.setLastModifiedBy(0L);
        userEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity2.setPassword(",");
        userEntity2.setRoles(new HashSet<>());
        userEntity2.setUsername(",");

        ArrayList<UserEntity> content = new ArrayList<>();
        content.add(userEntity2);
        content.add(userEntity);
        PageImpl<UserEntity> pageImpl = new PageImpl<>(content);
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(pageImpl);
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "ROLE_"});
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, details.size());
        assertEquals(2, data.getData().size());
        assertEquals(2L, data.getTotalItems());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll4() throws NotFoundException {
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "desc"});
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(0L, data.getTotalItems());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, details.size());
        assertTrue(data.getData().isEmpty());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll5() throws NotFoundException {
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any()))
                .thenThrow(new UserExistsException("An error occurred"));
        assertThrows(UserExistsException.class, () -> userService.findAll(1, 3, new String[]{"Sort", "ROLE_"}));
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll6() throws NotFoundException {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getId()).thenReturn(1L);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getUsername()).thenReturn("janedoe");
        when(userEntity.getCreatedDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userEntity.getLastModifiedDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userEntity.getRoles()).thenReturn(new HashSet<>());
        doNothing().when(userEntity).setCreatedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setDeleted(anyBoolean());
        doNothing().when(userEntity).setLastModifiedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setEmail(Mockito.<String>any());
        doNothing().when(userEntity).setId(Mockito.<Long>any());
        doNothing().when(userEntity).setPassword(Mockito.<String>any());
        doNothing().when(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        doNothing().when(userEntity).setUsername(Mockito.<String>any());
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");

        ArrayList<UserEntity> content = new ArrayList<>();
        content.add(userEntity);
        PageImpl<UserEntity> pageImpl = new PageImpl<>(content);
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(pageImpl);
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "ROLE_"});
        verify(userEntity).getCreatedDate();
        verify(userEntity).getLastModifiedDate();
        verify(userEntity).setCreatedBy(Mockito.<Long>any());
        verify(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).setDeleted(anyBoolean());
        verify(userEntity).setLastModifiedBy(Mockito.<Long>any());
        verify(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).getEmail();
        verify(userEntity).getId();
        verify(userEntity).getRoles();
        verify(userEntity).getUsername();
        verify(userEntity).setEmail(Mockito.<String>any());
        verify(userEntity).setId(Mockito.<Long>any());
        verify(userEntity).setPassword(Mockito.<String>any());
        verify(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        verify(userEntity).setUsername(Mockito.<String>any());
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, data.getData().size());
        assertEquals(1, details.size());
        assertEquals(1L, data.getTotalItems());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll7() throws NotFoundException {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setCreatedBy(1L);
        roleEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        roleEntity.setDeleted(true);
        roleEntity.setId(1);
        roleEntity.setLastModifiedBy(1L);
        roleEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        roleEntity.setName(Role.ADMIN);

        HashSet<RoleEntity> roleEntitySet = new HashSet<>();
        roleEntitySet.add(roleEntity);
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getId()).thenReturn(1L);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getUsername()).thenReturn("janedoe");
        when(userEntity.getCreatedDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userEntity.getLastModifiedDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userEntity.getRoles()).thenReturn(roleEntitySet);
        doNothing().when(userEntity).setCreatedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setDeleted(anyBoolean());
        doNothing().when(userEntity).setLastModifiedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setEmail(Mockito.<String>any());
        doNothing().when(userEntity).setId(Mockito.<Long>any());
        doNothing().when(userEntity).setPassword(Mockito.<String>any());
        doNothing().when(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        doNothing().when(userEntity).setUsername(Mockito.<String>any());
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");

        ArrayList<UserEntity> content = new ArrayList<>();
        content.add(userEntity);
        PageImpl<UserEntity> pageImpl = new PageImpl<>(content);
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(pageImpl);
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "ROLE_"});
        verify(userEntity).getCreatedDate();
        verify(userEntity).getLastModifiedDate();
        verify(userEntity).setCreatedBy(Mockito.<Long>any());
        verify(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).setDeleted(anyBoolean());
        verify(userEntity).setLastModifiedBy(Mockito.<Long>any());
        verify(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).getEmail();
        verify(userEntity).getId();
        verify(userEntity).getRoles();
        verify(userEntity).getUsername();
        verify(userEntity).setEmail(Mockito.<String>any());
        verify(userEntity).setId(Mockito.<Long>any());
        verify(userEntity).setPassword(Mockito.<String>any());
        verify(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        verify(userEntity).setUsername(Mockito.<String>any());
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, data.getData().size());
        assertEquals(1, details.size());
        assertEquals(1L, data.getTotalItems());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int, String[])}
     */
    @Test
    void testFindAll8() throws NotFoundException {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setCreatedBy(1L);
        roleEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        roleEntity.setDeleted(true);
        roleEntity.setId(1);
        roleEntity.setLastModifiedBy(1L);
        roleEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        roleEntity.setName(Role.ADMIN);

        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setCreatedBy(0L);
        roleEntity2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        roleEntity2.setDeleted(false);
        roleEntity2.setId(2);
        roleEntity2.setLastModifiedBy(0L);
        roleEntity2.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        roleEntity2.setName(Role.USER);

        HashSet<RoleEntity> roleEntitySet = new HashSet<>();
        roleEntitySet.add(roleEntity2);
        roleEntitySet.add(roleEntity);
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getId()).thenReturn(1L);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getUsername()).thenReturn("janedoe");
        when(userEntity.getCreatedDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userEntity.getLastModifiedDate()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(userEntity.getRoles()).thenReturn(roleEntitySet);
        doNothing().when(userEntity).setCreatedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setDeleted(anyBoolean());
        doNothing().when(userEntity).setLastModifiedBy(Mockito.<Long>any());
        doNothing().when(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        doNothing().when(userEntity).setEmail(Mockito.<String>any());
        doNothing().when(userEntity).setId(Mockito.<Long>any());
        doNothing().when(userEntity).setPassword(Mockito.<String>any());
        doNothing().when(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        doNothing().when(userEntity).setUsername(Mockito.<String>any());
        userEntity.setCreatedBy(1L);
        userEntity.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setId(1L);
        userEntity.setLastModifiedBy(1L);
        userEntity.setLastModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setPassword("iloveyou");
        userEntity.setRoles(new HashSet<>());
        userEntity.setUsername("janedoe");

        ArrayList<UserEntity> content = new ArrayList<>();
        content.add(userEntity);
        PageImpl<UserEntity> pageImpl = new PageImpl<>(content);
        when(userRepository.findAllByDeletedFalse(Mockito.<Pageable>any())).thenReturn(pageImpl);
        GenericResponse<DataTablesResponse<TableUserDto>> actualFindAllResult = userService.findAll(1, 3,
                new String[]{"Sort", "ROLE_"});
        verify(userEntity).getCreatedDate();
        verify(userEntity).getLastModifiedDate();
        verify(userEntity).setCreatedBy(Mockito.<Long>any());
        verify(userEntity).setCreatedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).setDeleted(anyBoolean());
        verify(userEntity).setLastModifiedBy(Mockito.<Long>any());
        verify(userEntity).setLastModifiedDate(Mockito.<LocalDateTime>any());
        verify(userEntity).getEmail();
        verify(userEntity).getId();
        verify(userEntity).getRoles();
        verify(userEntity).getUsername();
        verify(userEntity).setEmail(Mockito.<String>any());
        verify(userEntity).setId(Mockito.<Long>any());
        verify(userEntity).setPassword(Mockito.<String>any());
        verify(userEntity).setRoles(Mockito.<Set<RoleEntity>>any());
        verify(userEntity).setUsername(Mockito.<String>any());
        verify(userRepository).findAllByDeletedFalse(Mockito.<Pageable>any());
        ResultResponse result = actualFindAllResult.getResult();
        List<DetailResponse> details = result.getDetails();
        DetailResponse getResult = details.get(0);
        assertEquals("200 OK", getResult.getDetail());
        assertEquals("200 OK", getResult.getMessage());
        assertEquals("200", getResult.getInternalCode());
        assertEquals("Internal component details", result.getSource());
        DataTablesResponse<TableUserDto> data = actualFindAllResult.getData();
        assertEquals(0, data.getCurrentPage());
        assertEquals(1, data.getTotalPages());
        assertEquals(1, data.getData().size());
        assertEquals(1, details.size());
        assertEquals(1L, data.getTotalItems());
    }
}

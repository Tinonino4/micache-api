package com.api.micache_api.security.service;

import com.api.micache_api.security.controller.dto.AuthCreateRoleRequest;
import com.api.micache_api.security.controller.dto.AuthCreateUserRequest;
import com.api.micache_api.security.persistence.entity.*;
import com.api.micache_api.security.persistence.repository.RoleRepository;
import com.api.micache_api.security.persistence.repository.UserRepository;
import com.api.micache_api.security.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    private UserDetailServiceImpl userDetailService;

    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    private PermissionEntity permission;
    private Set<PermissionEntity> permissions;
    private RoleEntity role;
    private Set<RoleEntity> roles;
    private UserProfileEntity userProfile;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        userDetailService = new UserDetailServiceImpl(
            jwtUtils,
            passwordEncoder,
            userRepository,
            roleRepository
        );

        permission = createPermission();
        permissions = new HashSet<>(Collections.singletonList(permission));

        role = createRoleUser(permissions);
        roles = new HashSet<>(Collections.singletonList(role));

        userProfile = UserProfileEntity.builder()
                .id(1L)
                .build();
    }

    @Test
    public void whenUserNameExists_thenReturnUser() {
        String username = "name";
        UserEntity userMock = createUserEntityMock();
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.of(userMock));

        UserDetails result = userDetailService.loadUserByUsername(username);

        assertEquals(result.getUsername(), userMock.getUsername());
    }

    private UserEntity createUserEntityMock() {
        return createUserEntity(userProfile);
    }

    private UserEntity createUserEntity(UserProfileEntity userProfile) {
        return UserEntity.builder()
                .id(1L)
                .username("name")
                .password("password")
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(roles)
                .profile(userProfile)
                .build();
    }

    private static PermissionEntity createPermission() {
        return PermissionEntity.builder()
                .id(1L)
                .name("permission")
                .build();
    }

    @Test
    public void whenUserNoExists_thenThrow() {
        assertThrows( UsernameNotFoundException.class,
                () -> userDetailService.loadUserByUsername("")
        );
    }

    @Test
    public void whenRequestOk_thenReturnAuthResponse() {
        AuthCreateUserRequest request = new AuthCreateUserRequest(
                "username",
                "password",
                new AuthCreateRoleRequest(List.of(RoleEnum.USER.name(), RoleEnum.ADMIN.name())));
        RoleEntity roleUser = createRoleUser(permissions);

        when(roleRepository.findRoleEntitiesByRoleEnumIn(any())).thenReturn(roles.stream().toList());
        when(userRepository.save(any())).thenReturn(createUserEntity(userProfile));
        userDetailService.createUser(request);
    }

    private static RoleEntity createRoleUser(Set<PermissionEntity> permissions) {
        return RoleEntity.builder()
                .id(1L)
                .roleEnum(RoleEnum.USER)
                .permissionList(null)
                .build();
    }
}
package com.api.micache_api.security.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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



    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        userDetailService = new UserDetailServiceImpl(
            jwtUtils,
            passwordEncoder,
            userRepository,
            roleRepository
        );
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
        PermissionEntity permission = PermissionEntity.builder()
                .id(1L)
                .name("permission")
                .build();
        Set<PermissionEntity> permissions = new HashSet<>(Collections.singletonList(permission));

        RoleEntity role = RoleEntity.builder()
                .id(1L)
                .roleEnum(RoleEnum.USER)
                .permissionList(permissions)
                .build();
        Set<RoleEntity> roles = new HashSet<>(Collections.singletonList(role));

        UserProfileEntity userProfile = UserProfileEntity.builder()
                .id(1L)
                .build();

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
}
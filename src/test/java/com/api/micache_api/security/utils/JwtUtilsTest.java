package com.api.micache_api.security.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {
    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Test
    public void testCreateToken_validAuthentication_returnsToken() {
        // Mock authentication details
        String username = "testuser";
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getPrincipal()).thenReturn(username);
        doReturn(authorities).when(authentication).getAuthorities();

        // Mock properties (optional, if not injected from environment)
        String privateKey = "your_private_key";
        String userGenerator = "your_issuer";
        // Use @Spy or reflection if these are injected from environment
        ReflectionTestUtils.setField(jwtUtils, "privateKey",
                privateKey);
        ReflectionTestUtils.setField(jwtUtils, "userGenerator",
                userGenerator);
        // Call the method
        String token = jwtUtils.createToken(authentication);

        // Assertions
        assertNotNull(token);
        // You can further validate specific parts of the token using a JWT library like JJWT
    }

    @Test
    public void testCreateToken_nullAuthentication_throwsException() {
        // No need to mock authentication here
        assertThrows(IllegalArgumentException.class, () -> jwtUtils.createToken(null));
    }
}
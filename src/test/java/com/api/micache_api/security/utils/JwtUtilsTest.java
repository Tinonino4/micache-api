package com.api.micache_api.security.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
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

        // @Value attributes
        String privateKey = "your_private_key";
        String userGenerator = "your_issuer";

        ReflectionTestUtils.setField(jwtUtils, "privateKey",
                privateKey);
        ReflectionTestUtils.setField(jwtUtils, "userGenerator",
                userGenerator);

        String token = jwtUtils.createToken(authentication);

        assertNotNull(token);
    }

    @Test
    public void testCreateToken_nullAuthentication_throwsException() {
        // No need to mock authentication here
        assertThrows(IllegalArgumentException.class, () -> jwtUtils.createToken(null));
    }

    @Test
    public void whenValidToken_thenvalidateToken_returnJwtDecoded() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsIm5iZiI6MTcxODU0NjcwNywiaXNzIjoieW91cl9pc3N1ZXIiLCJleHAiOjE3MTg1NDg1MDcsImlhdCI6MTcxODU0NjcwNywiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJqdGkiOiJkNTc3ZmM5MC1jNjk2LTQwZTItOWEzYi1kNWIyNDRhZTg1MDUifQ.NQu1jUQPCgYzGW5AmqsxWAT9qwp970w7dD3JHbGg7g4";
        // @Value attributes
        String privateKey = "your_private_key";
        String userGenerator = "your_issuer";

        ReflectionTestUtils.setField(jwtUtils, "privateKey",
                privateKey);
        ReflectionTestUtils.setField(jwtUtils, "userGenerator",
                userGenerator);
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        assertTrue(decodedJWT.getSubject().equals("testuser"));
    }
}
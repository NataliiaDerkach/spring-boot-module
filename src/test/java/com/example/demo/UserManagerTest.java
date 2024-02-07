package com.example.demo;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRepository;
import com.example.demo.service.UserManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserManagerTest {
    @Autowired
    private UserManager userManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    public void changePasswordSuccess() throws Exception {
        String username = "user2@example.com";
        String oldPassword = "user2_password";
        String newPassword = "newPassword";
        String encodedNewPassword = "encodedNewPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(oldPassword));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        Authentication auth = new UsernamePasswordAuthenticationToken(username, oldPassword);
        SecurityContextHolder.getContext().setAuthentication(auth);

        userManager.changePassword(oldPassword, newPassword);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();
        assertEquals(encodedNewPassword, updatedUser.getPassword());

        SecurityContextHolder.clearContext();
    }
}

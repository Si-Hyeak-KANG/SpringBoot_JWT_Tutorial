package me.james.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.james.tutorial.dto.UserDto;
import me.james.tutorial.entity.Authority;
import me.james.tutorial.entity.User;
import me.james.tutorial.repository.UserRepository;
import me.james.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(UserDto userDto) {
        String currName = userDto.getUsername();
        userRepository.findOneWithAuthoritiesByUsername(currName)
                .ifPresent(e -> {
                    throw new RuntimeException("이미 가입된 유저입니다.");
                });

        Authority roles = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(currName)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(roles))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}

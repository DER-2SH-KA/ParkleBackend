package ru.d2k.parkle.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.repository.RoleRepository;
import ru.d2k.parkle.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);

        UserDetails userDetails;
        if (user.isPresent()) {
            userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(username)
                    .password(user.get().getPassword())
                    .roles(user.get().getRole().getName())
                    .build();
        }
        else {
            throw new UsernameNotFoundException(String.format("User not found with username (login): %s", username));
        }

        return userDetails;
    }
}

package ru.d2k.parkle.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.model.domain.User;
import ru.d2k.parkle.model.security.CustomUserDetails;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserDao dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userCache = dao.getByLogin(username);

        if (userCache.isPresent()) {
            return new CustomUserDetails(userCache.get());
        }
        else {
            throw new UsernameNotFoundException(String.format("UserEntity not found with username (login): %s", username));
        }
    }
}
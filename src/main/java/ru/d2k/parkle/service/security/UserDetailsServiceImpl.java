package ru.d2k.parkle.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.model.CustomUserDetails;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCache> user = userDao.getByLogin(username);

        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        }
        else {
            throw new UsernameNotFoundException(String.format("User not found with username (login): %s", username));
        }
    }
}
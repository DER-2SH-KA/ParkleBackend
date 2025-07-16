package ru.d2k.parkle.utils.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.repository.UserRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserResolver {

    private final UserRepository userRepository;

    public UUID resolve(User user) {
        if (user == null) return null;

        return user.getId();
    }
}

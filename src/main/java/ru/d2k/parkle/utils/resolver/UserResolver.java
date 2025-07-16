package ru.d2k.parkle.utils.resolver;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.repository.UserRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserResolver {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User resolve(UUID userId) {
        if (userId == null) return null;

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with ID: " + userId)
                );
    }

}

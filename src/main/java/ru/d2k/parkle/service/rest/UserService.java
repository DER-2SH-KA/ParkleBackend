package ru.d2k.parkle.service.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.repository.RoleRepository;
import ru.d2k.parkle.repository.UserRepository;
import ru.d2k.parkle.utils.mapper.UserMapper;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Return all users from DB as DTO.
     * @return Set of dto.
     * **/
    @Transactional(readOnly = true)
    public Set<UserResponseDto> findUsers() {
        log.info("Finding all users...");

        Set<UserResponseDto> dtos = userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toSet());

        log.info("Users was founded: {}", dtos.size());
        return dtos;
    }

    /**
     * Return user by ID as DTO.
     * @param id ID of user.
     * @return {@link UserResponseDto} dto.
     * **/
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(UUID id) {
        log.info("Finding user by ID: {}...", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User was not found with ID: " + id)
                );

        log.info("User with ID = {} was founded", id);
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(UserCreateDto dto) {
        log.info("Creating user: {}...", dto.toString());

        Role role = roleRepository.findById(dto.getRoleId()).orElseThrow(() ->
                new EntityNotFoundException("Role was not found with ID: " + dto.getRoleId())
        );
        User user = User.create(role, dto.getLogin(), dto.getEmail(), dto.getPassword());
        user = userRepository.save(user);

        log.info("User was created: {}", user);
        return userMapper.toResponseDto(user);
    }
}

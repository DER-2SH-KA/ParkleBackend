package ru.d2k.parkle.service.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dto.UserAuthDto;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
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
     * @return Set of {@link UserResponseDto}.
     * **/
    @Transactional(readOnly = true)
    public Set<UserResponseDto> findUsers() {
        log.info("Getting all users...");

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
        log.info("Getting user by ID: {}...", id);

        if (id == null) return null;

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User was not found with ID: " + id)
                );

        log.info("User with ID = {} was founded", id);
        return userMapper.toResponseDto(user);
    }

    /**
     * Authentication user by login and password.
     * @param uadto {@link UserAuthDto} object with login and password data.
     * @return {@link UserResponseDto} object. Can be null.
     * @throws EntityNotFoundException if user with given login and password was not found.
     * **/
    @Transactional(readOnly = true)
    public UserResponseDto authentication(UserAuthDto uadto) {
        log.info("Authenticate user by login and password. Login: {}", uadto.getLogin());

        UserResponseDto dto;

        User user = userRepository.findByLoginAndPassword(uadto.getLogin(), uadto.getPassword())
                .orElseThrow(() ->
                        new EntityNotFoundException("User was not found with login: {} and password" + uadto.getLogin())
                );
        dto = userMapper.toResponseDto(user);

        log.info("Authenticated user with DTO: {}", dto);
        return dto;
    }

    /**
     * Create user from DTO and return as DTO.
     * @param dto {@link UserCreateDto} of new user.
     * @return {@link UserResponseDto} dto.
     * **/
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

    /**
     * Update user from DTO and return as DTO.
     * @param udto {@link UserUpdateDto} dto of user to update.
     * @return {@link UserResponseDto} dto.
     * **/
    @Transactional
    public UserResponseDto updateUser(UUID id, UserUpdateDto udto) {
        log.info("Updating user by ID: {}...", id);

        if (id == null) return null;

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User was not found with ID: " + id)
                );

        userMapper.updateByDto( user, udto );
        user = userRepository.save(user);

        log.info("User with ID = {} was updated", id);
        return userMapper.toResponseDto(user);
    }

    /**
     * Delete user by ID.
     * @param id ID of user to delete.
     * **/
    @Transactional
    public void deleteUser(UUID id) {
        log.info("Deleting user by ID: {}...", id);

        if (Objects.nonNull(id)) {
            userRepository.deleteById(id);

            log.info("User with ID = {} was deleted", id);
        }
        else log.info("User ID equals null and not was deleted");
    }
}

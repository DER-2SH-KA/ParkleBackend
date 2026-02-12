package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.utils.mapper.UserMapper;

@Slf4j
@RequiredArgsConstructor
@Service
// TODO: Убрать ручное логирование, заменив их AOP и аннотациями.
public class UserService {
    private final RoleDao roleDao;
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Set<UserResponseDto> findUsers() {
        log.info("Getting all users...");

        Set<UserResponseDto> dtos = userDao.getAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toSet());

        log.info("Users was founded: {}", dtos.size());
        return dtos;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserByLogin(String login) {
        log.info("Getting user by login '{}'...", login);

        if (Objects.isNull(login)) return null;

        UserCache user = userDao.getByLogin(login)
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with login: " + login)
                );

        log.info("User with login '{}' was founded", login);
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(UserCreateDto dto) {
        log.info("Creating user: {}...", dto.toString());

        RoleCache role = roleDao.getByName(dto.getRoleName()).orElseThrow(() ->
                new RoleNotFoundException("Role was not found with Name: " + dto.getRoleName())
        );

        User user = User.create(
                new Role(role.id(), role.name(), role.priority()),
                dto.getLogin(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword())
        );
        UserCache savedUser = userDao.create(user);

        log.info("User was created: {}", user);
        return userMapper.toResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto updateUser(String login, UserUpdateDto udto) {
        log.info("Updating user by login '{}'...", login);

        Optional<UserCache> updatedUser = userDao.update(login, udto);

        UserResponseDto dto = userMapper.toResponseDto(updatedUser.orElseThrow(
                () -> new UserNotFoundException("User with login '{}' not found and not updated!")
        ));

        log.info("User with login '{}' was updated", login);
        return dto;
    }

    @Transactional
    public boolean deleteUser(String login) {
        log.info("Deleting user by login '{}'...", login);

        if (Objects.nonNull(login)) {

            if (userDao.deleteByLogin(login)) {
                log.info("User with login '{}' was deleted", login);
                return true;
            }
            else log.info("User with login '{}' wasn't deleted", login);

        }
        else log.error("User login equals null");

        return false;
    }

    public Optional<UserResponseDto> getUserByUserCache(
            UserCache cache
    ) {
        log.info("Start to getUserByUserCache() (login): {}", cache.login());

        UserResponseDto dto = userMapper.toResponseDto(cache);

        log.info("User was taken in getUserByUserCache() (login): {}", cache.login());
        return Optional.ofNullable(dto);
    }

    public Optional<UserResponseDto> getUserByJwt(String jwt) {
        Optional<UserResponseDto> dto = Optional.empty();

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        UserCache userCache =
                ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .getCache();

        if (Objects.nonNull(userCache)) {
            dto = Optional.of(userMapper.toResponseDto(userCache));
        }

        return dto;
    }
}

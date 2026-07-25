package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dto.UserCreateDto;
import ru.d2k.parkle.dto.UserResponseDto;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.utils.mapper.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final RoleDao roleDao;

    @Autowired
    private final UserDao dao;

    @Autowired
    private final UserMapper mapper;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto findByLogin(String login) {
        log.info("Getting user by login '{}'...", login);

        if (Objects.isNull(login)) {
            return null;
        }

        UserCache user = dao.getByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User was not found with login: " + login));

        log.info("User with login '{}' was founded", login);

        return mapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto create(UserCreateDto dto) {
        log.info("Creating user: {}...", dto.toString());

        RoleCache role = roleDao.getByName(dto.getRoleName()).orElseThrow(() ->
                new RoleNotFoundException("Role was not found with Name: " + dto.getRoleName()));

        User user = User.create(new Role(role.id(), role.name(), role.priority()), dto.getLogin(), dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()));
        UserCache savedUser = dao.create(user);

        log.info("User was created: {}", user);

        return mapper.toResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto updateByLogin(String login, UserUpdateDto updateUserDto) {
        log.info("Updating user by login '{}'...", login);

        Optional<UserCache> updatedUser = dao.updateByLogin(login, updateUserDto);

        UserResponseDto dto = mapper.toResponseDto(updatedUser.orElseThrow(() ->
                new UserNotFoundException("User with login '{}' not found and not updated!")));

        log.info("User with login '{}' was updated", login);

        return dto;
    }

    @Transactional
    public boolean deleteByLogin(String login) {
        log.info("Deleting user by login '{}'...", login);

        if (login != null) {
            if (dao.deleteByLogin(login)) {
                log.info("User with login '{}' was deleted", login);

                return true;
            } else {
                log.error("User with login '{}' wasn't deleted", login);
            }

        } else {
            log.error("User login equals null");
        }

        return false;
    }

    public Optional<UserResponseDto> getUserByUserCache(UserCache cache) {
        log.info("Start to getUserByUserCache() (login): {}", cache.login());

        UserResponseDto dto = mapper.toResponseDto(cache);

        log.info("User was taken in getUserByUserCache() by login: {}", cache.login());

        return Optional.ofNullable(dto);
    }

    public Optional<UserResponseDto> getUserByJwt(String jwt) {
        Optional<UserResponseDto> dto = Optional.empty();

        log.debug(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        UserCache userCache = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getCache();

        if (userCache != null) {
            dto = Optional.of(mapper.toResponseDto(userCache));
        }

        return dto;
    }
}
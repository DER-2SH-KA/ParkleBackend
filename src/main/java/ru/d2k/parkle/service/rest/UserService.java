package ru.d2k.parkle.service.rest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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
import ru.d2k.parkle.service.security.authentication.CustomAuthenticationManagerService;
import ru.d2k.parkle.utils.jwt.JwtUtil;
import ru.d2k.parkle.utils.mapper.UserMapper;

@Slf4j
@RequiredArgsConstructor
@Service
// TODO: Удалить дублирующие функционал методы.
// TODO: Убрать ручное логирование, заменив их AOP и аннотациями.
public class UserService {
    private final RoleDao roleDao;
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final CustomAuthenticationManagerService authenticationManagerService;
    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Return all users from DB as DTO.
     * @return Set of {@link UserResponseDto}.
     * **/
    @Transactional(readOnly = true)
    public Set<UserResponseDto> findUsers() {
        log.info("Getting all users...");

        Set<UserResponseDto> dtos = userDao.getAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toSet());

        log.info("Users was founded: {}", dtos.size());
        return dtos;
    }

    /**
     * Return user by ID as DTO.
     * @param id ID of user.
     * @return {@link UserResponseDto} dto.
     * @throws UserNotFoundException if user was not found by ID.
     * **/
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(UUID id) {
        log.info("Getting user by ID {}...", id);

        if (id == null) return null;

        UserCache user = userDao.getById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with ID: " + id)
                );

        log.info("User with ID {} was founded", id);
        return userMapper.toResponseDto(user);
    }

    /**
     * Return user by login as DTO.
     * @param login User's login.
     * @return {@link UserResponseDto} dto.
     * @throws UserNotFoundException if user not found by login.
     * */
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

    /**
     * Authentication user by login and password.
     * @param uadto {@link UserAuthDto} object with login and password data.
     * @return {@link UserResponseDto} object. Can be null.
     * @throws UserNotFoundException if user with given login and password was not found.
     * **/
    // TODO: Удалить.
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByAuthDao(UserAuthDto uadto) {
        log.info("Authenticate user by login '{}' and password", uadto.getLogin());

        UserCache user = userDao.getByLogin(uadto.getLogin())
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with login: {} and password in repository" + uadto.getLogin())
                );

        if (!passwordEncoder.matches(uadto.getPassword(), user.hashedPassword())) {
            throw new BadCredentialsException(
                    String.format("For User with login: %s given wrong password", uadto.getLogin())
            );
        }

        Optional<UserResponseDto> dto = Optional.ofNullable(userMapper.toResponseDto(user));

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

    /**
     * Update user from DTO and return as DTO.
     * @param login User's login.
     * @param udto {@link UserUpdateDto} dto of user to update.
     * @return {@link UserResponseDto} dto.
     * **/
    @Transactional
    public UserResponseDto updateUser(String login, UserUpdateDto udto) {
        log.info("Updating user by login '{}'...", login);

        if (Objects.isNull(login)) return null;

        UserCache user = userDao.getByLogin(login)
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with login: " + login)
                );
        Role role = null;

        if (udto.getRoleName() != null) {
            RoleCache roleCache = roleDao.getByName(udto.getRoleName())
                    .orElseThrow(() ->
                            new RoleNotFoundException("Role was not found with name: " + udto.getRoleName())
                    );

            role = new Role(roleCache.id(), roleCache.name(), roleCache.priority());
        }

        Optional<UserCache> updatedUser = userDao.update(login, udto);

        UserResponseDto dto = userMapper.toResponseDto(updatedUser.orElseThrow(
                () -> new UserNotFoundException("User with login '{}' not found and not updated!")
        ));

        log.info("User with login '{}' was updated", login);
        return dto;
    }

    /**
     * Delete user by ID.
     * @param login User's login to delete.
     * **/
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

    @Transactional
    public Optional<UserResponseDto> getUserByUserAuthDto(
            UserAuthDto uadto,
            HttpServletResponse response
    ) {
        log.info("Start to getUserByUserAuthDto(): {}", uadto);

        // TODO: Перенести отдельно в класс управлением авторизации.
        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(uadto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.getResponseCookieWithJwt(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        log.info("User was taken in getUserByUserAuthDto(): {}", uadto);
        return Optional.ofNullable(userMapper.toResponseDto(userDetails.getCache()));
    }

    @Transactional
    public Optional<UserResponseDto> getUserByUserCreateDto(
            UserCreateDto cdto,
            HttpServletResponse response

    ) {
        log.info("Start to getUserByUserCreateDto(): {}", cdto);
        this.createUser(cdto);

        // TODO: Перенести отдельно в класс управлением авторизации.
        Authentication signedAuthentication =
                authenticationManagerService.createHttpUnauthorizedAuthentication(cdto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.getResponseCookieWithJwt(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        log.info("User was taken in getUserByUserCreateDto(): {}", cdto);
        return Optional.ofNullable(userMapper.toResponseDto(userDetails.getCache()));
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

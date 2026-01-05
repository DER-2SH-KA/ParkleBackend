package ru.d2k.parkle.service.rest;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.RoleDao;
import ru.d2k.parkle.dto.*;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.entity.User;
import ru.d2k.parkle.entity.cache.RoleCache;
import ru.d2k.parkle.exception.JwtNotExistInRequestException;
import ru.d2k.parkle.exception.RoleNotFoundException;
import ru.d2k.parkle.exception.UserNotFoundException;
import ru.d2k.parkle.exception.UserWrongPasswordException;
import ru.d2k.parkle.model.CustomUserDetails;
import ru.d2k.parkle.repository.RoleRepository;
import ru.d2k.parkle.repository.UserRepository;
import ru.d2k.parkle.service.security.JwtService;
import ru.d2k.parkle.utils.jwt.JwtUtil;
import ru.d2k.parkle.utils.mapper.UserMapper;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final RoleDao roleDao;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    private final RedisTemplate<String, Object> redisTemplate;

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
     * @throws UserNotFoundException if user was not found by ID.
     * **/
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(UUID id) {
        log.info("Getting user by ID: {}...", id);

        if (id == null) return null;

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with ID: " + id)
                );

        log.info("User with ID = {} was founded", id);
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
        log.info("Getting user by login: {}...", login);

        if (Objects.isNull(login)) return null;

        User user = userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with login: " + login)
                );

        log.info("User with login = {} was founded", login);
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
        log.info("Authenticate user by login and password. Login: {}", uadto.getLogin());

        User user = userRepository.findByLogin(uadto.getLogin())
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found with login: {} and password in repository" + uadto.getLogin())
                );

        if (!passwordEncoder.matches(uadto.getPassword(), user.getPassword())) {
            throw new UserWrongPasswordException(
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
        user = userRepository.save(user);

        log.info("User was created: {}", user);
        return userMapper.toResponseDto(user);
    }

    /**
     * Update user from DTO and return as DTO.
     * @param login User's login.
     * @param udto {@link UserUpdateDto} dto of user to update.
     * @return {@link UserResponseDto} dto.
     * **/
    @Transactional
    public UserResponseDto updateUser(String login, UserUpdateDto udto) {
        log.info("Updating user by login: {}...", login);

        if (Objects.isNull(login)) return null;

        User user = userRepository.findByLogin(login)
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

        userMapper.updateByDto( user, udto, role);
        user = userRepository.save(user);

        log.info("User with login = {} was updated", login);
        return userMapper.toResponseDto(user);
    }

    /**
     * Delete user by ID.
     * @param login User's login to delete.
     * **/
    @Transactional
    public boolean deleteUser(String login) {
        log.info("Deleting user by login: {}...", login);

        if (Objects.nonNull(login)) {

            if (!userRepository.existsByLogin(login)) {
                log.info("User with login = {} is already not exists", login);
                return false;
            }

            userRepository.deleteByLogin(login);

            if (!userRepository.existsByLogin(login)) {
                log.info("User with login = {} was deleted", login);
                return true;
            }
            else log.info("User with login = {} wasn't deleted", login);
        }
        else log.info("User login equals null and not was deleted");

        return false;
    }

    @Transactional
    public Optional<UserResponseDto> getUserByUserAuthDto(
            UserAuthDto uadto,
            HttpServletResponse response
    ) {
        log.info("Start to getUserByUserAuthDto(): {}", uadto);

        Authentication signedAuthentication = this.createAuthenticationByDto(uadto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.getResponseCookieWithJwt(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return Optional.ofNullable(userMapper.toResponseDto(userDetails.getEntity()));
    }

    @Transactional
    public Optional<UserResponseDto> getUserByUserCreateDto(
            UserCreateDto cdto,
            HttpServletResponse response

    ) {
        this.createUser(cdto);

        Authentication signedAuthentication = this.createAuthenticationByDto(cdto);
        CustomUserDetails userDetails = (CustomUserDetails) signedAuthentication.getPrincipal();

        String jwtToken = jwtUtil.generateToken(userDetails);

        ResponseCookie jwtCookie = jwtUtil.getResponseCookieWithJwt(jwtToken);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        // TODO: Эксперементально взаимодействую с Redis. Удалить.
        UserResponseDto dto = userMapper.toResponseDto(userDetails.getEntity());

        if (Objects.nonNull(dto)) {

            final String sliceKey = "users:";
            final String key = sliceKey + dto.id();

            redisTemplate.opsForValue().set(key, dto, Duration.ofHours(1));

            return Optional.ofNullable(dto);
        }

        return Optional.empty();
    }

    public Optional<UserResponseDto> getUserByJwt(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Optional<UserResponseDto> dto = Optional.empty();

        try {
            Optional<String> jwt = JwtUtil.extractJwtFromCookie(request);

            if (jwt.isPresent()) {
                dto = jwtService.getUserUuidByJwtToken(request);

                log.info(dto.isPresent() ? dto.get().toString() : "DTO None!");

                ResponseCookie jwtCookie = jwtUtil.getResponseCookieWithJwt(jwt.get());
                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            }
            else throw new JwtNotExistInRequestException("JWT token in Cookie is empty or null!");
        }
        catch (ExpiredJwtException ex) {
            log.error("User's JWT is expired!");

            ResponseCookie expiredCookie = jwtUtil.createJwtExpiredCookie();
            response.addHeader( HttpHeaders.SET_COOKIE, expiredCookie.toString());
        }
        catch (Exception ex) {
            log.error("Exception in getUserByJwt()!: {}", ex.getMessage());
            ResponseCookie expiredCookie = jwtUtil.createJwtExpiredCookie();
            response.addHeader( HttpHeaders.SET_COOKIE, expiredCookie.toString());
        }

        return dto;
    }

    public void userLogout(HttpServletResponse response) {
        ResponseCookie logoutCookie = jwtUtil.createJwtExpiredCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, logoutCookie.toString());
    }

    public Authentication createAuthenticationByDto(UserDto uadto) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        uadto.getLogin(),
                        uadto.getPassword()
                )
        );
    }
}

package ru.d2k.parkle.service.rest;

import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.cache.UserCache;

public interface UserService {

    UserCache updateByLogin(String login, UserUpdateDto updateUserDto);
    boolean deleteByLogin(String login);
}
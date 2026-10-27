package ru.d2k.parkle.service.rest;

import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.model.domain.User;

public interface UserService {

    User updateByLogin(String login, UserUpdateDto updateUserDto);
    void deleteByLogin(String login);
    boolean existsByLogin(String login);
}
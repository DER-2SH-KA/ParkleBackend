package ru.d2k.parkle.service.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.cache.UserCache;
import ru.d2k.parkle.service.rest.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao dao;

    @Override
    @Transactional
    public UserCache updateByLogin(String login, UserUpdateDto updateUserDto) {
        return dao.updateByLogin(login, updateUserDto);
    }

    @Override
    @Transactional
    public void deleteByLogin(String login) {
        log.debug("Deleting user by login '{}'...", login);

        if (login != null) {
            dao.deleteByLogin(login);
        } else {
            log.error("UserEntity login equals null");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByLogin(String login) {
        return dao.existsByLogin(login);
    }
}
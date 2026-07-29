package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.dao.UserDao;
import ru.d2k.parkle.dto.UserUpdateDto;
import ru.d2k.parkle.entity.cache.UserCache;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserDao dao;

    @Transactional
    public UserCache updateByLogin(String login, UserUpdateDto updateUserDto) {
        return dao.updateByLogin(login, updateUserDto);
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
}
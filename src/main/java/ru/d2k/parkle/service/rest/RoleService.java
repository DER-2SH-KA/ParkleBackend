package ru.d2k.parkle.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.d2k.parkle.repository.RoleRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    @Autowired
    private final RoleRepository ROLE_REPOSITORY;
}

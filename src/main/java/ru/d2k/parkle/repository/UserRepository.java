package ru.d2k.parkle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.d2k.parkle.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
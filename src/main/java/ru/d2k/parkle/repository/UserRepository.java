package ru.d2k.parkle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.d2k.parkle.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.websites WHERE u.login = :login")
    Optional<User> findByLogin(@Param("login") String login);
    boolean existsById(UUID id);
    boolean existsByLogin(String login);
    void deleteByLogin(String login);
}
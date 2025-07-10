package ru.d2k.parkle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.d2k.parkle.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}

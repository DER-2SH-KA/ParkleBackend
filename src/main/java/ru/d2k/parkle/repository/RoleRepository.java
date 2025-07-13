package ru.d2k.parkle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.d2k.parkle.entity.Role;

import java.util.HashMap;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("SELECT r.id, r FROM Role r WHERE( r.name LIKE :roleName )")
    HashMap<Integer, Role> findRolesByName(@Param("roleName") String roleName);
}

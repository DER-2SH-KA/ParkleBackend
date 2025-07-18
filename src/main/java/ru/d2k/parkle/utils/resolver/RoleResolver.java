package ru.d2k.parkle.utils.resolver;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleResolver {

    private final RoleRepository roleRepository;

    @Named("fromUuidToRole")
    @Transactional(readOnly = true)
    public Role resolve(UUID roleId) {
        if (roleId == null) return null;

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with ID: " + roleId)
                );
    }

}

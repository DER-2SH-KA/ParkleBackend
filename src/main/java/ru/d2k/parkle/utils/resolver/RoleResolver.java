package ru.d2k.parkle.utils.resolver;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.d2k.parkle.entity.Role;
import ru.d2k.parkle.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class RoleResolver {

    private final RoleRepository roleRepository;

    @Named("fromNameToRole")
    @Transactional(readOnly = true)
    public Role resolve(String roleName) {
        if (roleName == null || roleName.isBlank()) return null;

        return roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new EntityNotFoundException("Role not found with Name: " + roleName)
                );
    }

}

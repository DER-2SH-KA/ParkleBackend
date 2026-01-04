package ru.d2k.parkle.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private RoleResponseDto role;
    private String login;
    private String email;

}

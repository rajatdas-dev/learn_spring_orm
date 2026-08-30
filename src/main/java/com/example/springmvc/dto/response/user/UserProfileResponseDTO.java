package com.example.springmvc.dto.response.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDTO {

    private Long id;
    private String phoneNumber;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private String bio;
}

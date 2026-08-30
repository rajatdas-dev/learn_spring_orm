package com.example.springmvc.dto.request.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequestDTO {

    private String phoneNumber;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private String bio;
}

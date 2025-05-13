package com.tq.comic.dto.response.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tq.comic.constant.RoleEnum;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;

    String dob;
    Integer isActive;
    String picture;
    Set<RoleEnum> roles;
}

package com.tq.comic.dto.request.authentication;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationRequest {

    String username;

    String password;

    String firstName;

    String lastName;

    String email;

    LocalDate dob;

    Integer isActive;
}

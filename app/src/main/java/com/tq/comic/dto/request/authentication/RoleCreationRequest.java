package com.tq.comic.dto.request.authentication;

import com.tq.comic.constant.RoleEnum;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleCreationRequest {
    RoleEnum roleName;
}

package com.tq.comic.dto.response.other;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FavoriteResponse {
    String username;
    String storyCode;
    boolean check;
}

package com.tq.comic.dto.response.other;

import com.tq.comic.dto.response.user.UserResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentResponse {
    String id;

    String content;

    String username;

    String storyId;

    int depth;

    Date createAt;

    List<CommentResponse> replies = new ArrayList<>();

    public CommentResponse(String username, String content){
        this.username = username;
        this.content= content;
    }
}

package com.tq.comic.dto.response.story;

import com.tq.comic.constant.StatusEnum;
import com.tq.comic.dto.response.chapter.ChapterComponentResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StoryResponse {

    String id;

    String title;

    String code;

    String author;

    String description;

    StatusEnum status;

    String coverImage;

    Date createdAt;

    Date updatedAt;

    Set<String> generates = new HashSet<>();

    List<ChapterComponentResponse> chapters = new ArrayList<>();
}

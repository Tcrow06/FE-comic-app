package com.tq.comic.dto.response.story;

import android.os.Parcel;
import android.os.Parcelable;

import com.tq.comic.constant.StatusEnum;
import com.tq.comic.dto.response.chapter.ChapterComponentResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.*;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StoryResponse implements Parcelable {

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
    List<ChapterComponentResponse> chapters;

    int totalChapter;
    boolean isSave;

    // Phương thức tạo đối tượng từ Parcel
    protected StoryResponse(Parcel in) {
        id = in.readString();
        title = in.readString();
        code = in.readString();
        author = in.readString();
        description = in.readString();
        status = (StatusEnum) in.readSerializable(); // Giả sử StatusEnum implement Serializable
        coverImage = in.readString();
        createdAt = new Date(in.readLong()); // Nếu cần có kiểu Date, đọc long và chuyển đổi
        updatedAt = new Date(in.readLong());
        generates = new HashSet<>(in.createStringArrayList());
        chapters = in.createTypedArrayList(ChapterComponentResponse.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(code);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeSerializable(status); // Ghi giá trị StatusEnum
        dest.writeString(coverImage);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1); // Ghi thời gian dưới dạng long
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
        dest.writeStringList(new ArrayList<>(generates));
        dest.writeTypedList(chapters);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // CREATOR để phục hồi đối tượng từ Parcel
    public static final Creator<StoryResponse> CREATOR = new Creator<StoryResponse>() {
        @Override
        public StoryResponse createFromParcel(Parcel in) {
            return new StoryResponse(in);
        }

        @Override
        public StoryResponse[] newArray(int size) {
            return new StoryResponse[size];
        }
    };
}


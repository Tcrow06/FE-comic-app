package com.tq.comic.dto.response.chapter;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChapterComponentResponse implements Parcelable {

    String title;
    String date;
    Integer chapterNumber;
    String storyId;

    // Phương thức tạo đối tượng từ Parcel
    protected ChapterComponentResponse(Parcel in) {
        title = in.readString();
        date = in.readString();
        chapterNumber = in.readInt();
        storyId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeInt(chapterNumber != null ? chapterNumber : -1); // Nếu null, ghi giá trị -1
        dest.writeString(storyId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // CREATOR để phục hồi đối tượng từ Parcel
    public static final Creator<ChapterComponentResponse> CREATOR = new Creator<ChapterComponentResponse>() {
        @Override
        public ChapterComponentResponse createFromParcel(Parcel in) {
            return new ChapterComponentResponse(in);
        }

        @Override
        public ChapterComponentResponse[] newArray(int size) {
            return new ChapterComponentResponse[size];
        }
    };
}

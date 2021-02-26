package com.v2ex.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Topic implements Parcelable {
    public Node node;
    public Member member;
    public int id;
    public String last_reply_by;
    public long last_touched;
    public String title;
    public String url;
    public long created;
    public String content;
    public long last_modified;
    public int replies;

    protected Topic(Parcel in) {
        node = in.readParcelable(Node.class.getClassLoader());
        member = in.readParcelable(Member.class.getClassLoader());
        id = in.readInt();
        last_reply_by = in.readString();
        last_touched = in.readLong();
        title = in.readString();
        url = in.readString();
        created = in.readLong();
        content = in.readString();
        last_modified = in.readLong();
        replies = in.readInt();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(node, flags);
        dest.writeParcelable(member, flags);
        dest.writeInt(id);
        dest.writeString(last_reply_by);
        dest.writeLong(last_touched);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeLong(created);
        dest.writeString(content);
        dest.writeLong(last_modified);
        dest.writeInt(replies);
    }
}

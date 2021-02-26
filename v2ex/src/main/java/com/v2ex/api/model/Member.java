package com.v2ex.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {
    public String username;
    public String website;
    public String github;
    public String psn;
    public String avatar_normal;
    public String bio;
    public String url;
    public String tagline;
    public String twitter;
    public long created;
    public String avatar_large;
    public String avatar_mini;
    public int id;

    protected Member(Parcel in) {
        username = in.readString();
        website = in.readString();
        github = in.readString();
        psn = in.readString();
        avatar_normal = in.readString();
        bio = in.readString();
        url = in.readString();
        tagline = in.readString();
        twitter = in.readString();
        created = in.readLong();
        avatar_large = in.readString();
        avatar_mini = in.readString();
        id = in.readInt();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(website);
        dest.writeString(github);
        dest.writeString(psn);
        dest.writeString(avatar_normal);
        dest.writeString(bio);
        dest.writeString(url);
        dest.writeString(tagline);
        dest.writeString(twitter);
        dest.writeLong(created);
        dest.writeString(avatar_large);
        dest.writeString(avatar_mini);
        dest.writeInt(id);
    }
}
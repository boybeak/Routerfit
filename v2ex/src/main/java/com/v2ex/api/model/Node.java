package com.v2ex.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Node implements Parcelable {
    public String avatar_large;
    public String name;
    public String avatar_normal;
    public String title;
    public String url;
    public int topics;
    public String footer;
    public String header;
    public String title_alternative;
    public String avatar_mini;
    public int stars;
    public boolean root;
    public int id;
    public String parent_node_name;

    protected Node(Parcel in) {
        avatar_large = in.readString();
        name = in.readString();
        avatar_normal = in.readString();
        title = in.readString();
        url = in.readString();
        topics = in.readInt();
        footer = in.readString();
        header = in.readString();
        title_alternative = in.readString();
        avatar_mini = in.readString();
        stars = in.readInt();
        root = in.readByte() != 0;
        id = in.readInt();
        parent_node_name = in.readString();
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar_large);
        dest.writeString(name);
        dest.writeString(avatar_normal);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeInt(topics);
        dest.writeString(footer);
        dest.writeString(header);
        dest.writeString(title_alternative);
        dest.writeString(avatar_mini);
        dest.writeInt(stars);
        dest.writeByte((byte) (root ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(parent_node_name);
    }
}

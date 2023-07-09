package com.pee.tokopee;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private Integer id;
    private String imageUrl;
    private String name;
    private String size;
    private Integer price;

    public Item(Integer id, String imageUrl, String name, Integer price, String size) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.name);
        dest.writeString(this.size);
        dest.writeValue(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Integer) source.readValue(Integer.class.getClassLoader());
        this.imageUrl = source.readString();
        this.name = source.readString();
        this.size = source.readString();
        this.price = (Integer) source.readValue(Integer.class.getClassLoader());
    }

    protected Item(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.imageUrl = in.readString();
        this.name = in.readString();
        this.size = in.readString();
        this.price = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}

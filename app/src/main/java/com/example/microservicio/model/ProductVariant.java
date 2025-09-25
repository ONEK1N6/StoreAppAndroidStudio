package com.example.microservicio.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class ProductVariant implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("size")
    private String size;

    @SerializedName("color")
    private String color;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("stock")
    private int stock;

    // Constructor vacío
    public ProductVariant() {}

    // --- Parcelable Implementation ---

    protected ProductVariant(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        size = in.readString();
        color = in.readString();
        imageUrl = in.readString();
        stock = in.readInt();
    }

    public static final Creator<ProductVariant> CREATOR = new Creator<ProductVariant>() {
        @Override
        public ProductVariant createFromParcel(Parcel in) {
            return new ProductVariant(in);
        }

        @Override
        public ProductVariant[] newArray(int size) {
            return new ProductVariant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(size);
        dest.writeString(color);
        dest.writeString(imageUrl);
        dest.writeInt(stock);
    }

    // --- Getters ---

    public Long getId() { return id; }
    public String getSize() { return size; }
    public String getColor() { return color; }
    public String getImageUrl() { return imageUrl; }
    public int getStock() { return stock; }
}
package com.example.microservicio.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Product implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    @SerializedName("imageUrls")
    private List<String> imageUrls;

    @SerializedName("category")
    private Category category;

    @SerializedName("variants")
    private List<ProductVariant> variants;

    // Constructor vacío
    public Product() {}

    // --- Parcelable Implementation ---

    protected Product(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imageUrls = in.createStringArrayList();
        category = in.readParcelable(Category.class.getClassLoader());
        variants = in.createTypedArrayList(ProductVariant.CREATOR);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
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
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeStringList(imageUrls);
        dest.writeParcelable(category, flags);
        dest.writeTypedList(variants);
    }

    // --- Getters ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public List<String> getImageUrls() { return imageUrls; }
    public Category getCategory() { return category; }
    public List<ProductVariant> getVariants() { return variants; }
}

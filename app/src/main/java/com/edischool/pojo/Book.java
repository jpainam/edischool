package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String authors;
    private String editors;
    private String title;
    private String price;
    private String edition;

    public Book(){

    }

    protected Book(Parcel in) {
        authors = in.readString();
        editors = in.readString();
        title = in.readString();
        price = in.readString();
        edition = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getEditors() {
        return editors;
    }

    public void setEditors(String editors) {
        this.editors = editors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authors);
        dest.writeString(editors);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(edition);
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
}

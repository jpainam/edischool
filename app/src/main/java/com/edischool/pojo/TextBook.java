package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TextBook implements Parcelable {
    private String formId;
    private String subject;
    private List<Book> books;

    public TextBook(){

    }
    protected TextBook(Parcel in) {
        formId = in.readString();
        subject = in.readString();
        books = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<TextBook> CREATOR = new Creator<TextBook>() {
        @Override
        public TextBook createFromParcel(Parcel in) {
            return new TextBook(in);
        }

        @Override
        public TextBook[] newArray(int size) {
            return new TextBook[size];
        }
    };

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formId);
        dest.writeString(subject);
        dest.writeTypedList(books);
    }
}

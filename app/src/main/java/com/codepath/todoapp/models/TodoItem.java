package com.codepath.todoapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TodoItem implements Parcelable {
    private String title;
    private String notes;
    private Date dueDate;
    private String priority;

    public enum Priority {
        HIGH ("High"), DEFAULT ("Default"), LOW ("Low");

        private String value;

        Priority (String sValue) {
            value = sValue;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public TodoItem() {
    }

    public TodoItem(String title, String notes, String priority, Date date) {
        setTitle(title);
        setNotes(notes);
        setPriority(priority);
        setDueDate(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.notes);
        dest.writeLong(dueDate != null ? dueDate.getTime() : -1);
        dest.writeString(this.priority);
    }

    private TodoItem(Parcel in) {
        this.title = in.readString();
        this.notes = in.readString();
        long tmpDueDate = in.readLong();
        this.dueDate = tmpDueDate == -1 ? null : new Date(tmpDueDate);
        this.priority = in.readString();
    }

    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        public TodoItem createFromParcel(Parcel source) {
            return new TodoItem(source);
        }

        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };
}

package com.codepath.todoapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TodoItem implements Parcelable {
    private int id;
    private String title;
    private String notes;
    private Date dueDate;
    private Priority priority;

    public enum Priority {
        HIGH ("High", 0), DEFAULT ("Default", 1), LOW ("Low", 2);

        private String stringValue;
        private int intValue;

        Priority (String sValue, int iValue) {
            stringValue = sValue;
            intValue = iValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        @Override
        public String toString() {
            return getStringValue();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getPriorityValue() {
        return priority.getIntValue();
    }

    public TodoItem() {
    }

    public TodoItem(String title, String notes, int priority, Date date) {
        setTitle(title);
        setNotes(notes);
        this.priority = priority == -1 ? null : Priority.values()[priority];
        setDueDate(date);
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", dueDate=" + dueDate +
                ", priority='" + priority + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.notes);
        dest.writeLong(dueDate != null ? dueDate.getTime() : -1);
        dest.writeInt(this.priority == null ? -1 : this.priority.ordinal());
    }

    private TodoItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.notes = in.readString();
        long tmpDueDate = in.readLong();
        this.dueDate = tmpDueDate == -1 ? null : new Date(tmpDueDate);
        int tmpPriority = in.readInt();
        this.priority = tmpPriority == -1 ? null : Priority.values()[tmpPriority];
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

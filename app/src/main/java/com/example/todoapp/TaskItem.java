package com.example.todoapp;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskItem implements Parcelable {

    private int key_id;
    private String title;
    private String description;
    private String createDate;
    private String finishDate;
    private String deadLineDate;
    private String createTime;
    private String finishTime;
    private String deadLineTime;
    private String isHidden;
    private String category;

    public TaskItem() {
    }

    public TaskItem(int key_id, String title, String description, String createDate, String createTime, String finishDate, String finishTime, String deadLineDate, String deadLineTime, String isHidden, String category) {
        this.key_id = key_id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.finishDate = finishDate;
        this.deadLineDate = deadLineDate;
        this.createTime = createTime;
        this.finishTime = finishTime;
        this.deadLineTime = deadLineTime;
        this.isHidden = isHidden;
        this.category = category;
    }

    protected TaskItem(Parcel in) {
        key_id = in.readInt();
        title = in.readString();
        description = in.readString();
        createDate = in.readString();
        finishDate = in.readString();
        deadLineDate = in.readString();
        createTime = in.readString();
        finishTime = in.readString();
        deadLineTime = in.readString();
        isHidden = in.readString();
        category = in.readString();
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(String deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public String getCreateTime() { return createTime; }

    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getFinishTime() { return finishTime; }

    public void setFinishTime(String finishTime) { this.finishTime = finishTime; }

    public String getDeadLineTime() { return deadLineTime; }

    public void setDeadLineTime(String deadLineTime) { this.deadLineTime = deadLineTime; }

    public String getIsHidden() { return isHidden; }

    public void setIsHidden(String isHidden) { this.isHidden = isHidden; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(key_id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(createDate);
        parcel.writeString(finishDate);
        parcel.writeString(deadLineDate);
        parcel.writeString(createTime);
        parcel.writeString(finishTime);
        parcel.writeString(deadLineTime);
        parcel.writeString(isHidden);
        parcel.writeString(category);
    }
}

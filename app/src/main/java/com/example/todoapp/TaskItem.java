package com.example.todoapp;

public class TaskItem {

    private int key_id;
    private String title;
    private String description;
    private String createDate;
    private String finishDate;
    private String deadLineDate;

    public TaskItem(int key_id, String title, String description, String createDate, String finishDate, String deadLineDate) {
        this.key_id = key_id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.finishDate = finishDate;
        this.deadLineDate = deadLineDate;
    }

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
}

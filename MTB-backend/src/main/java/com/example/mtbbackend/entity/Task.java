package com.example.mtbbackend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

@TableName("task")
public class Task {//数据库表task以及相应字段

    @TableId(type = IdType.INPUT)
    private String id;//uuid
    private String title;//任务标题
    private String description;//任务介绍
    private Boolean completed;//完成情况
    private LocalDateTime createTime;//创建时间
    private LocalDateTime finishedTime;//完成时间

    public Task(){
    }
    public Task(String title, String description, Boolean completed){
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(LocalDateTime finishedTime) {
        this.finishedTime = finishedTime;
    }

    public void setFinishedTime() {
        this.finishedTime = LocalDateTime.now();
    }
}

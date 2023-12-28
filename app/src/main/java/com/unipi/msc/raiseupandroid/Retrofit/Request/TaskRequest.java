package com.unipi.msc.raiseupandroid.Retrofit.Request;

import com.unipi.msc.raiseupandroid.Model.Difficulty;

import java.util.List;

public class TaskRequest {
    private String title;
    private String description;
    private Long dueTo;
    private Long columnId;
    private Boolean completed;
    private Difficulty difficulty;
    private List<Long> tagIds;
    private List<Long> employeeIds;

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

    public Long getDueTo() {
        return dueTo;
    }

    public void setDueTo(Long dueTo) {
        this.dueTo = dueTo;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    private TaskRequest(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.columnId = builder.columnId;
        this.dueTo = builder.dueTo;
        this.completed = builder.completed;
        this.difficulty = builder.difficulty;
        this.tagIds = builder.tagIds;
        this.employeeIds = builder.employeeIds;
    }

    public static class Builder {
        private String title;
        private String description;
        private Long columnId;

        private Long dueTo;
        private boolean completed;
        private Difficulty difficulty;
        private List<Long> tagIds;
        private List<Long> employeeIds;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDueTo(Long dueTo) {
            this.dueTo = dueTo;
            return this;
        }

        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder setTagIds(List<Long> tagIds) {
            this.tagIds = tagIds;
            return this;
        }

        public Builder setEmployeeIds(List<Long> employeeIds) {
            this.employeeIds = employeeIds;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setColumnId(Long columnId) {
            this.columnId = columnId;
            return this;
        }

        public TaskRequest build(){
            return new TaskRequest(this);
        }
    }
}

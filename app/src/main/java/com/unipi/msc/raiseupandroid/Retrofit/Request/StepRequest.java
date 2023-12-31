package com.unipi.msc.raiseupandroid.Retrofit.Request;

public class StepRequest {
    private String title;
    private Long position;

    public StepRequest(Builder builder) {
        title = builder.title;
        position = builder.position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
    public static class Builder{
        private String title;
        private Long position;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPosition(Long position) {
            this.position = position;
            return this;
        }
        public StepRequest build() {
            return new StepRequest(this);
        }
    }
}

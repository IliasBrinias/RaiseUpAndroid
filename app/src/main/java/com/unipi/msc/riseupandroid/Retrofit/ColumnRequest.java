package com.unipi.msc.riseupandroid.Retrofit;

public class ColumnRequest {
    private Long id;
    private Long position;

    public ColumnRequest(Long id, Long position) {
        this.id = id;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
}

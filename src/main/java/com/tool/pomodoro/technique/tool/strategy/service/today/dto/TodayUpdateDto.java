package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

public class TodayUpdateDto {
    private String id;
    private String content;
    private int clocks;

    public TodayUpdateDto() {
    }

    public TodayUpdateDto(String id, String content, int clocks) {
        this.id = id;
        this.content = content;
        this.clocks = clocks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClocks() {
        return clocks;
    }

    public void setClocks(int clocks) {
        this.clocks = clocks;
    }
}

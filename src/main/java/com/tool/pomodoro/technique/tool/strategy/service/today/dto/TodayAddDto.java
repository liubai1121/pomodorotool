package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

public class TodayAddDto {
    private String content;
    private int clocks;

    public TodayAddDto() {
    }

    public TodayAddDto(String content) {
        this.content = content;
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

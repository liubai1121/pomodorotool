package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

public record TodayAddDto(String content, int clocks) {
    public TodayAddDto(String content) {
        this(content, 0);
    }
}

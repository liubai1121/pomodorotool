package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

public record TodayAddDto(String content, int clocks, String category) {
    public TodayAddDto(String content, String category) {
        this(content, 0, category);
    }
}

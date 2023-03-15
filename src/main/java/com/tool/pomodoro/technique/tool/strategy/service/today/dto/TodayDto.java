package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

import java.time.LocalDateTime;

public record TodayDto(String id, String content, int clocks, LocalDateTime createTime) {
}

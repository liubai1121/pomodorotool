package com.tool.pomodoro.technique.tool.strategy.service.todo.dto;

import java.time.LocalDateTime;

public record TodoDto(String id, String content, LocalDateTime createTime) {
}

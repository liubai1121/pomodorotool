package com.tool.pomodoro.technique.tool.strategy.database.today.po;

import java.time.LocalDateTime;

public record Today(String id, String content, int clocks, LocalDateTime createTime) {

    public Today(String id, String content) {
        this(id, content, 0, LocalDateTime.now());
    }

    public Today(String id, String content, int clocks) {
        this(id, content, clocks, LocalDateTime.now());
    }
}

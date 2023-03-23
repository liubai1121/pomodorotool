package com.tool.pomodoro.technique.tool.strategy.storage.today.po;

import java.time.LocalDateTime;

public record Today(String id,
                    String content,
                    int clocks,
                    String category,
                    LocalDateTime createTime) {

    public Today(String id, String content, String category) {
        this(id, content, 0, category, LocalDateTime.now());
    }

    public Today(String id, String content, int clocks, String category) {
        this(id, content, clocks, category, LocalDateTime.now());
    }
}

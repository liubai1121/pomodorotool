package com.tool.pomodoro.technique.tool.strategy.storage.todo.po;

import java.time.LocalDateTime;

public record Todo(String id, String content, LocalDateTime createTime) {

    public Todo {
    }

    public Todo(String id, String content) {
        this(id, content, LocalDateTime.now());
    }
}

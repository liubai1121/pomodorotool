package com.tool.pomodoro.technique.tool.strategy.service.todo.dto;

public class TodoDto {
    private String id;
    private String content;

    public TodoDto() {
    }

    public TodoDto(String id, String content) {
        this.id = id;
        this.content = content;
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
}

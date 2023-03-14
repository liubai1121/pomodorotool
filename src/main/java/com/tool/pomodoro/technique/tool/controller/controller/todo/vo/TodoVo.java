package com.tool.pomodoro.technique.tool.controller.controller.todo.vo;

public class TodoVo {
    private String id;
    private String content;

    public TodoVo(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public TodoVo() {
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

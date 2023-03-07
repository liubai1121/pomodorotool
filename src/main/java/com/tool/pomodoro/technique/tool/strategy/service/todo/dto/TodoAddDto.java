package com.tool.pomodoro.technique.tool.strategy.service.todo.dto;

public class TodoAddDto {
    private String todo;

    public TodoAddDto() {
    }

    public TodoAddDto(String todo) {
        this.todo = todo;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
}

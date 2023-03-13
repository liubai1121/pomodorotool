package com.tool.pomodoro.technique.tool.strategy.database.todo.po;

import java.time.LocalDateTime;
import java.util.Objects;

public class Todo implements Cloneable {
    private String id;
    private String content;
    private LocalDateTime createTime;

    public Todo() {
    }

    public Todo(String id, String content, LocalDateTime createTime) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) && Objects.equals(content, todo.content) && Objects.equals(createTime, todo.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createTime);
    }

    @Override
    public Todo clone() {
        try {
            return (Todo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

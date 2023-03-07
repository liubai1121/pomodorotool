package com.tool.pomodoro.technique.tool.strategy.database.today.po;

import java.time.LocalDateTime;
import java.util.Objects;

public class Today {
    private String id;
    private String content;
    private int clocks;
    private LocalDateTime createTime;

    public Today() {
    }

    public Today(String id, String content, int clocks, LocalDateTime createTime) {
        this.id = id;
        this.content = content;
        this.clocks = clocks;
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

    public int getClocks() {
        return clocks;
    }

    public void setClocks(int clocks) {
        this.clocks = clocks;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Today today = (Today) o;
        return id.equals(today.id) && content.equals(today.content) && createTime.equals(today.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createTime);
    }
}

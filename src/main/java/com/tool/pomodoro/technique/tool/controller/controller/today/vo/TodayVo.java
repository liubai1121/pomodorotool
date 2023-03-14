package com.tool.pomodoro.technique.tool.controller.controller.today.vo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TodayVo {
    private final SimpleStringProperty id;
    private final SimpleStringProperty content;
    private final SimpleIntegerProperty clocks;

    public TodayVo(String id, String content, int clocks) {
        this.id = new SimpleStringProperty(id);
        this.content = new SimpleStringProperty(content);
        this.clocks = new SimpleIntegerProperty(clocks);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public int getClocks() {
        return clocks.get();
    }

    public void setClocks(int clocks) {
        this.clocks.set(clocks);
    }
}

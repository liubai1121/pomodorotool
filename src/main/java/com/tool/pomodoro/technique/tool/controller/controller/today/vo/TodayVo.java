package com.tool.pomodoro.technique.tool.controller.controller.today.vo;

import java.time.LocalDateTime;

public record TodayVo(String id, String content, int clocks, LocalDateTime createTime) {
}

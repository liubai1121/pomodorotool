package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

import java.util.List;

public record TodayStatisticsDto(List<TodayStatisticsBaseDto> todayList, int totalClocks, int totalTime) {

}

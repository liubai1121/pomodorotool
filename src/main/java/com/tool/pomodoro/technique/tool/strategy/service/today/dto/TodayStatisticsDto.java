package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record TodayStatisticsDto(List<TodayTableReportRecordDto> todayList,
                                 int totalClocks,
                                 int totalTime,
                                 Map<LocalDate, Integer> clocksPerDay) {

}

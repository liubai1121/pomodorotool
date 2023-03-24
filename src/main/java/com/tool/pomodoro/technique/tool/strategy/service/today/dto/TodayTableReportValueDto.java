package com.tool.pomodoro.technique.tool.strategy.service.today.dto;

import java.util.List;

public record TodayTableReportValueDto(List<TodayTableReportRecordDto> table, int totalClocks) {
}

package com.tool.pomodoro.technique.tool.strategy.service.today;

import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayLineChartReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportValueDto;

import java.time.LocalDate;
import java.util.Optional;

public interface TodayReportStrategy {
    Optional<TodayTableReportValueDto> table(LocalDate startDate, LocalDate endDate);

    Optional<TodayLineChartReportValueDto> lineChart(LocalDate startDate, LocalDate endDate);
}

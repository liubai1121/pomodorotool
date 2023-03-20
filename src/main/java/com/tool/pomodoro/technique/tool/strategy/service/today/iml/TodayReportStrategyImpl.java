package com.tool.pomodoro.technique.tool.strategy.service.today.iml;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayLineChartReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportRecordDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.storage.today.TodayStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TodayReportStrategyImpl implements TodayReportStrategy {

    private final TodayStorage todayStorage;

    public TodayReportStrategyImpl(TodayStorage todayStorage) {
        this.todayStorage = todayStorage;
    }


    @Override
    public Optional<TodayTableReportValueDto> table(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Optional.empty();
        }

        return todayStorage.getByDuration(startDate, endDate)
                .filter(Predicate.not(Collection::isEmpty))
                .map(this::wrapTableRecords);
    }

    private TodayTableReportValueDto wrapTableRecords(List<Today> todayList) {
        final var clocksTime = 25;

        var table = todayList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(Today::content),
                        map -> {
                            var list = new ArrayList<TodayTableReportRecordDto>();
                            map.forEach((content, groupByTodayList) -> {
                                int clocks = groupByTodayList.stream().mapToInt(Today::clocks).sum();
                                list.add(new TodayTableReportRecordDto(content, clocks, clocks * clocksTime));
                            });
                            return list;
                        }));

        var totalClock = table.stream().mapToInt(TodayTableReportRecordDto::clocks).sum();

        return new TodayTableReportValueDto(table, totalClock, totalClock * clocksTime);
    }

    @Override
    public Optional<TodayLineChartReportValueDto> lineChart(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Optional.empty();
        }

        return todayStorage.getByDuration(startDate, endDate)
                .filter(Predicate.not(Collection::isEmpty))
                .map(this::wrapLineChartValue);
    }

    private TodayLineChartReportValueDto wrapLineChartValue(List<Today> todayList) {
        List<String> xAxisList = new ArrayList<>();
        List<Number> yAxisList = new ArrayList<>();

        todayList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(today -> today.createTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.summingInt(Today::clocks)))
                .forEach((date, clocks) -> {
                    xAxisList.add(DateTimeFormatter.ISO_LOCAL_DATE.format(date));
                    yAxisList.add(clocks);
                });

        return new TodayLineChartReportValueDto(xAxisList.toArray(String[]::new), yAxisList.toArray(Number[]::new));
    }
}

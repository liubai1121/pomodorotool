package com.tool.pomodoro.technique.tool.strategy.service.today.wrapper;

import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayStatisticsBaseDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayStatisticsDto;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TodayStrategyWrapper {
    private TodayStrategyWrapper() {
    }

    public static List<TodayDto> wrapTodayDtos(List<Today> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(today -> new TodayDto(today.id(), today.content(), today.clocks(), today.category(),
                        today.createTime()))
                .collect(Collectors.toList());
    }

    public static Optional<TodayStatisticsDto> wrapTodayStatisticsDto(List<Today> todayList) {
        final var clocksTime = 25;

        var baseDtoList = todayList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(Today::content),
                        map -> {
                            var list = new ArrayList<TodayStatisticsBaseDto>();
                            map.forEach((content, groupByTodayList) -> {
                                int clocks = groupByTodayList.stream().mapToInt(Today::clocks).sum();
                                list.add(new TodayStatisticsBaseDto(content, clocks, clocks * clocksTime));
                            });
                            return list;
                        }));
        if (baseDtoList.isEmpty()) {
            return Optional.empty();
        }

        var totalClock = baseDtoList.stream().mapToInt(TodayStatisticsBaseDto::clocks).sum();

        Map<LocalDate, Integer> clocksPerDay = todayList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(today -> today.createTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.summingInt(Today::clocks)));

        return Optional.of(new TodayStatisticsDto(baseDtoList, totalClock, totalClock * clocksTime, clocksPerDay));
    }

}

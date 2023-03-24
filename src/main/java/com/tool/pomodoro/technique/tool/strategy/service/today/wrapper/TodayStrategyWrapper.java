package com.tool.pomodoro.technique.tool.strategy.service.today.wrapper;

import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;

import java.util.List;
import java.util.Objects;
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
}

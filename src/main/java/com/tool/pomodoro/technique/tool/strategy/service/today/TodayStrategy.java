package com.tool.pomodoro.technique.tool.strategy.service.today;

import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayStatisticsDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodayStrategy {
    String add(TodayAddDto dto);

    void delete(String uuid);

    void update(TodayUpdateDto updateDto);

    void incrementClock(String uuid);

    Optional<TodayDto> get(String uuid);

    Optional<List<TodayDto>> all();
}

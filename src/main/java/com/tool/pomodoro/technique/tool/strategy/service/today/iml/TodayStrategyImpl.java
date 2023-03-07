package com.tool.pomodoro.technique.tool.strategy.service.today.iml;

import com.tool.pomodoro.technique.tool.strategy.database.today.TodayDatabase;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.database.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TodayStrategyImpl implements TodayStrategy {

    private TodayDatabase todayDatabase;

    public TodayStrategyImpl(TodayDatabase todayDatabase) {
        this.todayDatabase = todayDatabase;
    }

    @Override
    public String add(TodayAddDto dto) {
        var uuid = UUID.randomUUID().toString();
        var today = new Today();
        today.setId(uuid);
        today.setContent(dto.getContent());
        today.setClocks(dto.getClocks());
        today.setCreateTime(LocalDateTime.now());
        todayDatabase.save(today);
        return uuid;
    }

    @Override
    public void delete(String id) {
        todayDatabase.deleteById(id);
    }

    @Override
    public void update(TodayUpdateDto updateDto) {
        var updateToday = new Today();
        updateToday.setId(updateDto.getId());
        updateToday.setContent(updateDto.getContent());
        updateToday.setClocks(updateDto.getClocks());
        todayDatabase.update(updateToday);
    }

    @Override
    public Optional<TodayDto> get(String uuid) {
        return todayDatabase.selectById(uuid)
                .map(today -> new TodayDto(today.getId(), today.getContent(), today.getClocks()));
    }

    @Override
    public Optional<List<TodayDto>> all() {
        return todayDatabase.selectAll()
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .map(today -> new TodayDto(today.getId(), today.getContent(), today.getClocks()))
                        .collect(Collectors.toList()));
    }

    @Override
    public void incrementClock(String uuid) {
        todayDatabase.selectById(uuid)
                .ifPresent(today -> {
                    var newClocks = today.getClocks() + 1;
                    Today updateToday = new Today();
                    updateToday.setId(today.getId());
                    updateToday.setClocks(newClocks);
                    todayDatabase.update(updateToday);
                });
    }
}

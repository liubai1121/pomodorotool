package com.tool.pomodoro.technique.tool.strategy.service.today.iml;

import com.tool.pomodoro.technique.tool.strategy.database.today.TodayDatabase;
import com.tool.pomodoro.technique.tool.strategy.database.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodayStrategyImpl implements TodayStrategy {

    private final TodayDatabase todayDatabase;

    public TodayStrategyImpl(TodayDatabase todayDatabase) {
        this.todayDatabase = todayDatabase;
    }

    @Override
    public String add(TodayAddDto dto) {
        var uuid = IdUtil.generate();
        var today = new Today(uuid, dto.content(), dto.clocks());
        todayDatabase.save(today);
        return uuid;
    }


    @Override
    public void delete(String id) {
        todayDatabase.deleteById(id);
    }

    @Override
    public void update(TodayUpdateDto dto) {
        var updateToday = new Today(dto.id(), dto.content(), dto.clocks());
        todayDatabase.update(updateToday);
    }

    @Override
    public Optional<TodayDto> get(String uuid) {
        return todayDatabase.selectById(uuid)
                .map(today -> new TodayDto(today.id(), today.content(), today.clocks(), today.createTime()));
    }

    @Override
    public Optional<List<TodayDto>> all() {
        return todayDatabase.selectAll()
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .map(today -> new TodayDto(today.id(), today.content(), today.clocks(), today.createTime()))
                        .collect(Collectors.toList()));
    }

    @Override
    public void incrementClock(String uuid) {
        todayDatabase.selectById(uuid)
                .ifPresent(today -> {
                    var newClocks = today.clocks() + 1;

                    Today updateToday = new Today(today.id(), today.content(), newClocks);
                    todayDatabase.update(updateToday);
                });
    }
}

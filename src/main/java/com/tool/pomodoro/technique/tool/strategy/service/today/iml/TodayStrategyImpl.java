package com.tool.pomodoro.technique.tool.strategy.service.today.iml;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.wrapper.TodayStrategyWrapper;
import com.tool.pomodoro.technique.tool.strategy.storage.today.TodayStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;

import java.util.List;
import java.util.Optional;

public class TodayStrategyImpl implements TodayStrategy {

    private final TodayStorage todayStorage;

    public TodayStrategyImpl(TodayStorage todayStorage) {
        this.todayStorage = todayStorage;
    }

    @Override
    public String add(TodayAddDto dto) {
        var uuid = IdUtil.generate();
        var today = new Today(uuid, dto.content(), dto.clocks());
        todayStorage.save(today);
        return uuid;
    }


    @Override
    public void delete(String id) {
        todayStorage.deleteById(id);
    }

    @Override
    public void update(TodayUpdateDto dto) {
        var updateToday = new Today(dto.id(), dto.content(), dto.clocks());
        todayStorage.update(updateToday);
    }

    @Override
    public void incrementClock(String uuid) {
        todayStorage.selectById(uuid)
                .ifPresent(today -> {
                    var newClocks = today.clocks() + 1;

                    Today updateToday = new Today(today.id(), today.content(), newClocks);
                    todayStorage.update(updateToday);
                });
    }

    @Override
    public Optional<TodayDto> get(String uuid) {
        return todayStorage.selectById(uuid)
                .map(today -> new TodayDto(today.id(), today.content(), today.clocks(), today.createTime()));
    }

    @Override
    public Optional<List<TodayDto>> all() {
        return todayStorage.selectAll()
                .map(TodayStrategyWrapper::wrapTodayDtos);
    }
}

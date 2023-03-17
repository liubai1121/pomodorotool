package com.tool.pomodoro.technique.tool.strategy.service.today.iml;

import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayStatisticsDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.wrapper.TodayStrategyWrapper;
import com.tool.pomodoro.technique.tool.strategy.storage.today.TodayStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.util.IdUtil;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;

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

    @Override
    public Optional<TodayStatisticsDto> getByDay(LocalDate today) {
        return Optional.ofNullable(today)
                .flatMap(todayStorage::getByDay)
                .flatMap(TodayStrategyWrapper::wrapTodayStatisticsDto);
    }

    @Override
    public Optional<TodayStatisticsDto> getByWeek(int week) {
        var maxWeek = 53;
        var minWeek = 1;
        boolean isWrongRange = week < minWeek || week > maxWeek;
        if (isWrongRange) {
            return Optional.empty();
        }

        LocalDate now = LocalDate.now();
        int nowWeek = now.get(WeekFields.ISO.weekOfWeekBasedYear());

        LocalDate weeKDate = now.minusWeeks(nowWeek - week);
        var weekFirstDay = weeKDate.with((temporal) -> temporal.with(DAY_OF_WEEK, 1));
        var weekLastDay = weeKDate.with((temporal) -> temporal.with(DAY_OF_WEEK, temporal.range(DAY_OF_WEEK).getMaximum()));

        return todayStorage.getByDuration(weekFirstDay, weekLastDay)
                .flatMap(TodayStrategyWrapper::wrapTodayStatisticsDto);
    }

    @Override
    public Optional<TodayStatisticsDto> getByMonth(int month) {
        var maxMonth = 12;
        var minMonth = 1;
        boolean isWrongRange = month < minMonth || month > maxMonth;
        if (isWrongRange) {
            return Optional.empty();
        }

        LocalDate monthDate = LocalDate.now().withMonth(month);

        var monthFirstDay = monthDate.with(TemporalAdjusters.firstDayOfMonth());
        var monthLastDay = monthDate.with(TemporalAdjusters.lastDayOfMonth());

        return todayStorage.getByDuration(monthFirstDay, monthLastDay)
                .flatMap(TodayStrategyWrapper::wrapTodayStatisticsDto);
    }

    @Override
    public Optional<TodayStatisticsDto> getByDuration(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Optional.empty();
        }
        return todayStorage.getByDuration(startDate, endDate)
                .flatMap(TodayStrategyWrapper::wrapTodayStatisticsDto);
    }
}

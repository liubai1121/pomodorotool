package com.tool.pomodoro.technique.tool.strategy.service.today;

import com.tool.pomodoro.technique.tool.factory.FileStorageStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TodayStrategyTests {
    private static TodayStrategy todayStrategy;

    @BeforeAll
    static void init() {
        StrategyFactory strategyFactory = new FileStorageStrategyFactory(new TestFilePathConfig());
        todayStrategy = strategyFactory.createTodayStrategy();
    }

    @Test
    void add() {
        var clocks = 2;
        var dto = new TodayAddDto("开发任务", clocks);
        var uuid = todayStrategy.add(dto);

        Optional<TodayDto> today = todayStrategy.get(uuid);
        Assertions.assertTrue(today.isPresent());
        Assertions.assertEquals(clocks, today.get().clocks());
    }

    @Test
    void delete() {
        var dto = new TodayAddDto("测试删除");
        var id = todayStrategy.add(dto);
        Assertions.assertTrue(todayStrategy.get(id).isPresent());

        todayStrategy.delete(id);
        Assertions.assertFalse(todayStrategy.get(id).isPresent());
    }

    @Test
    void update() {
        var dto = new TodayAddDto("测试更新方法");
        String id = todayStrategy.add(dto);

        var updateContent = "测试更新成功";
        var updateClocks = 15;
        var updateDto = new TodayUpdateDto(id, updateContent, updateClocks);
        todayStrategy.update(updateDto);

        Optional<TodayDto> today = todayStrategy.get(id);

        Assertions.assertTrue(today.isPresent());
        Assertions.assertEquals(today.get().content(), updateContent);
        Assertions.assertEquals(today.get().clocks(), updateClocks);
    }

    @Test
    void selectAll() {
        var dto = new TodayAddDto("自测");
        todayStrategy.add(dto);

        Optional<List<TodayDto>> today = todayStrategy.all();
        Assertions.assertTrue(today.isPresent());
        Assertions.assertFalse(today.get().isEmpty());
    }

    @Test
    void incrementClock() {
        var dto = new TodayAddDto("测试自增时钟");
        String id = todayStrategy.add(dto);

        todayStrategy.incrementClock(id);
        Optional<TodayDto> todayDtpOpt = todayStrategy.get(id);

        Assertions.assertTrue(todayDtpOpt.isPresent());
        Assertions.assertEquals(todayDtpOpt.get().clocks(), 1);

        todayStrategy.incrementClock(id);

        todayDtpOpt = todayStrategy.get(id);

        Assertions.assertTrue(todayDtpOpt.isPresent());
        Assertions.assertEquals(todayDtpOpt.get().clocks(), 2);
    }

    @Test
    void getByDay() {

        todayStrategy.add(new TodayAddDto("测试根据日期查询"));

        var today = LocalDate.now();
        var todayListOpt = todayStrategy.getByDay(today);

        Assertions.assertTrue(todayListOpt.isPresent());
        Assertions.assertFalse(todayListOpt.get().todayList().isEmpty());
    }


    @Test
    void getByWeek() {
        todayStrategy.add(new TodayAddDto("测试根据星期查询"));

        var week = 11;
        var todayListOpt = todayStrategy.getByWeek(week);

        Assertions.assertTrue(todayListOpt.isPresent());
        Assertions.assertFalse(todayListOpt.get().todayList().isEmpty());
    }

    @Test
    void getByWeekForWrongRange() {
        todayStrategy.add(new TodayAddDto("测试根据星期查询-错误的范围"));

        var week = 0;
        var todayListOpt = todayStrategy.getByWeek(week);

        Assertions.assertTrue(todayListOpt.isEmpty());
    }

    @Test
    void getByMonth() {
        todayStrategy.add(new TodayAddDto("测试根据月份查询"));

        var month = 3;
        var todayListOpt = todayStrategy.getByMonth(month);

        Assertions.assertTrue(todayListOpt.isPresent());
    }

    @Test
    void getByMonthForWrongRange() {
        todayStrategy.add(new TodayAddDto("测试根据月份查询-错误的范围"));

        var month = 0;
        var todayListOpt = todayStrategy.getByMonth(month);

        Assertions.assertTrue(todayListOpt.isEmpty());
    }

    @Test
    void getByDuration() {
        todayStrategy.add(new TodayAddDto("测试根据时间段查询"));

        var today = LocalDate.now();
        var weekAgo = today.minusWeeks(1);
        var todayListOpt = todayStrategy.getByDuration(weekAgo, today);

        Assertions.assertTrue(todayListOpt.isPresent());
        Assertions.assertFalse(todayListOpt.get().todayList().isEmpty());
    }
}

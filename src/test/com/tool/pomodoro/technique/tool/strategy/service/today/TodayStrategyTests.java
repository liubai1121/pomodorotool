package com.tool.pomodoro.technique.tool.strategy.service.today;

import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.factory.today.TodayStrategyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class TodayStrategyTests {

    TodayStrategy todayStrategy = TodayStrategyFactory.create();

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
}

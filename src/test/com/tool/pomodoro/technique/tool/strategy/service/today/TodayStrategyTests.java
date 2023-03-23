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
import java.util.Random;

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
        var dto = new TodayAddDto("开发任务", clocks, "测试添加");
        var uuid = todayStrategy.add(dto);

        Optional<TodayDto> today = todayStrategy.get(uuid);
        Assertions.assertTrue(today.isPresent());
        Assertions.assertEquals(clocks, today.get().clocks());
    }

    @Test
    void delete() {
        var id = doAdd();
        Assertions.assertTrue(todayStrategy.get(id).isPresent());

        todayStrategy.delete(id);
        Assertions.assertFalse(todayStrategy.get(id).isPresent());
    }

    @Test
    void update() {
        String id = doAdd();

        var updateContent = "测试更新成功";
        var updateCategory = "测试更新成功";
        var updateClocks = 15;
        var updateDto = new TodayUpdateDto(id, updateContent, updateClocks, updateCategory);
        todayStrategy.update(updateDto);

        Optional<TodayDto> today = todayStrategy.get(id);

        Assertions.assertTrue(today.isPresent());
        Assertions.assertEquals(today.get().content(), updateContent);
        Assertions.assertEquals(today.get().clocks(), updateClocks);
    }

    @Test
    void selectAll() {
        doAdd();

        Optional<List<TodayDto>> today = todayStrategy.all();
        Assertions.assertTrue(today.isPresent());
        Assertions.assertFalse(today.get().isEmpty());
    }

    @Test
    void incrementClock() {
        String id = doAdd();

        todayStrategy.incrementClock(id);
        Optional<TodayDto> todayDtpOpt = todayStrategy.get(id);

        Assertions.assertTrue(todayDtpOpt.isPresent());
        Assertions.assertEquals(todayDtpOpt.get().clocks(), 1);

        todayStrategy.incrementClock(id);

        todayDtpOpt = todayStrategy.get(id);

        Assertions.assertTrue(todayDtpOpt.isPresent());
        Assertions.assertEquals(todayDtpOpt.get().clocks(), 2);
    }

    private String doAdd() {
        var random = new Random();
        var idx = random.nextInt(10000);
        return todayStrategy.add(new TodayAddDto("添加" + idx, "测试分类"));
    }
}

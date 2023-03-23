package com.tool.pomodoro.technique.tool.strategy.service.today;

import com.tool.pomodoro.technique.tool.factory.FileStorageStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.storage.file.TestFilePathConfig;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayLineChartReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportValueDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

public class TodayReportStrategyTests {

    private static TodayReportStrategy todayReportStrategy;
    private static TodayStrategy todayStrategy;


    @BeforeAll
    static void init() {
        StrategyFactory strategyFactory = new FileStorageStrategyFactory(new TestFilePathConfig());
        todayReportStrategy = strategyFactory.createTodayReportStrategy();
        todayStrategy = strategyFactory.createTodayStrategy();
    }

    @Test
    void table() {
        doAddToday();


        Optional<TodayTableReportValueDto> tableValueOpt = todayReportStrategy.table(LocalDate.now(), LocalDate.now());

        Assertions.assertTrue(tableValueOpt.isPresent());
        Assertions.assertTrue(tableValueOpt.get().table().size() > 0);
    }

    @Test
    void tableForWrongRange() {
        doAddToday();

        var today = LocalDate.now();
        var yesterday = today.minusDays(1);
        var todayListOpt = todayReportStrategy.table(today, yesterday);

        Assertions.assertTrue(todayListOpt.isEmpty());
    }

    @Test
    void lineChart() {
        doAddToday();

        Optional<TodayLineChartReportValueDto> lineChartOpt = todayReportStrategy.lineChart(LocalDate.now(), LocalDate.now());
        Assertions.assertTrue(lineChartOpt.isPresent());
        Assertions.assertTrue(lineChartOpt.get().xAxis().length > 0);
        Assertions.assertEquals(lineChartOpt.get().xAxis().length, lineChartOpt.get().yAxis().length);
    }

    @Test
    void lineChartForWrongRange() {
        doAddToday();

        var today = LocalDate.now();
        var yesterday = today.minusDays(1);
        var todayListOpt = todayReportStrategy.lineChart(today, yesterday);

        Assertions.assertTrue(todayListOpt.isEmpty());
    }

    private String doAddToday() {
        var random = new Random();
        var idx = random.nextInt(10000);
        return todayStrategy.add(new TodayAddDto("添加" + idx, "测试分类"));
    }
}

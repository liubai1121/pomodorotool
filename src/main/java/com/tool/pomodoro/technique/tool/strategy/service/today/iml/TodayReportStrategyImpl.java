package com.tool.pomodoro.technique.tool.strategy.service.today.iml;

import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayLineChartReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayPieChartForCategoryReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportRecordDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportValueDto;
import com.tool.pomodoro.technique.tool.strategy.storage.today.TodayStorage;
import com.tool.pomodoro.technique.tool.strategy.storage.today.po.Today;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TodayReportStrategyImpl implements TodayReportStrategy {

    private final TodayStorage todayStorage;

    public TodayReportStrategyImpl(TodayStorage todayStorage) {
        this.todayStorage = todayStorage;
    }

    @Override
    public Optional<TodayTableReportValueDto> table(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Optional.empty();
        }

        return todayStorage.getByDuration(startDate, endDate)
                .filter(Predicate.not(Collection::isEmpty))
                .map(this::wrapTableRecords);
    }

    private TodayTableReportValueDto wrapTableRecords(List<Today> todayList) {
        var table = todayList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(Today::content),
                        map -> {
                            var list = new ArrayList<TodayTableReportRecordDto>();
                            map.forEach((content, groupByTodayList) -> {
                                int clocks = groupByTodayList.stream().mapToInt(Today::clocks).sum();
                                String category = groupByTodayList.stream().map(Today::category).filter(Objects::nonNull).distinct().collect(Collectors.joining(","));
                                list.add(new TodayTableReportRecordDto(content, clocks, category));
                            });
                            return list;
                        }));

        var totalClock = table.stream().mapToInt(TodayTableReportRecordDto::clocks).sum();

        return new TodayTableReportValueDto(table, totalClock);
    }

    @Override
    public Optional<TodayLineChartReportValueDto> lineChart(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Optional.empty();
        }

        return todayStorage.getByDuration(startDate, endDate)
                .filter(Predicate.not(Collection::isEmpty))
                .map(this::wrapLineChartValue);
    }

    private TodayLineChartReportValueDto wrapLineChartValue(List<Today> todayList) {
        List<String> xAxisList = new ArrayList<>();
        List<Number> yAxisList = new ArrayList<>();

        todayList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(today -> today.createTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.summingInt(Today::clocks)))
                .forEach((date, clocks) -> {
                    xAxisList.add(DateTimeFormatter.ISO_LOCAL_DATE.format(date));
                    yAxisList.add(clocks);
                });

        return new TodayLineChartReportValueDto(xAxisList.toArray(String[]::new), yAxisList.toArray(Number[]::new));
    }

    @Override
    public Optional<TodayPieChartForCategoryReportValueDto> pieChartForCategory(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return Optional.empty();
        }

        return todayStorage.getByDuration(startDate, endDate)
                .filter(Predicate.not(Collection::isEmpty))
                .map(this::wrapPieChartForCategory);
    }

    private TodayPieChartForCategoryReportValueDto wrapPieChartForCategory(List<Today> todayList) {
        var mapForCategory = todayList.stream()
                .filter(Objects::nonNull)
                .filter(today -> Objects.nonNull(today.category()) && !today.category().isBlank())
                .collect(Collectors.groupingBy(Today::category));

        var totalSize = new BigDecimal(todayList.size());

        Map<String, Integer> dataMap = new HashMap<>();
        mapForCategory.forEach((category, dataList) -> {
            var categorySize = new BigDecimal(dataList.size());
            var proportion = categorySize.divide(totalSize, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();
            dataMap.put(category, proportion);
        });

        var categoryList = mapForCategory.values().stream().flatMap(Collection::stream).toList();
        var totalCategorySize = new BigDecimal(categoryList.size());
        var categorySizeEqualsTotalSize = totalCategorySize.compareTo(totalSize) == 0;
        if (categorySizeEqualsTotalSize) {
            return new TodayPieChartForCategoryReportValueDto(dataMap);
        }

        var notCategorySize = totalSize.subtract(totalCategorySize);
        var notCategoryProportion = notCategorySize.divide(totalSize, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();
        dataMap.put("未分类", notCategoryProportion);

        return new TodayPieChartForCategoryReportValueDto(dataMap);
    }
}

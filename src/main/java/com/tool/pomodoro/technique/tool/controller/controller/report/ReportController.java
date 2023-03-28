package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.controller.util.TypeConversionUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;

public class ReportController implements Initializable {

    private final StrategyFactory strategyFactory;
    private final TodayReportStrategy todayReportStrategy;

    public ReportController(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
        this.todayReportStrategy = strategyFactory.createTodayReportStrategy();
        ;
    }

    @FXML
    TextField queryByDay;
    @FXML
    ComboBox<String> queryByWeek;
    @FXML
    ComboBox<String> queryByMonth;
    @FXML
    TextField queryByDurationForStart;
    @FXML
    TextField queryByDurationForEnd;

    @FXML
    ChoiceBox<String> choiceBox;

    @FXML
    protected void onChoiceBoxSelected() {
        Optional.ofNullable(choiceBox.getSelectionModel())
                .map(SingleSelectionModel::getSelectedItem)
                .flatMap(QueryType::contentOf)
                .ifPresent(queryType -> {
                    hidesAllQueryFields();

                    switch (queryType) {
                        case DAY -> {
                            queryByDay.setVisible(true);
                            var yesterday = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().minusDays(1));
                            queryByDay.setText(yesterday);
                        }

                        case WEEK -> {
                            queryByWeek.setVisible(true);
                            var week = LocalDate.now().get(ChronoField.ALIGNED_WEEK_OF_YEAR);
                            var defaultWeek = week - 1 < 1 ? week : week - 1;
                            Optional.ofNullable(queryByWeek.getSelectionModel())
                                    .ifPresent(selectionModel -> selectionModel.select(defaultWeek - 1));
                        }

                        case MONTH -> {
                            queryByMonth.setVisible(true);
                            var month = LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
                            var defaultMonth = month - 1 < 1 ? month : month - 1;
                            Optional.ofNullable(queryByMonth.getSelectionModel())
                                    .ifPresent(selectionModel -> selectionModel.select(defaultMonth - 1));
                        }

                        case DURATION -> {
                            queryByDurationForStart.setVisible(true);
                            queryByDurationForEnd.setVisible(true);

                            var today = LocalDate.now();
                            var yesterday = DateTimeFormatter.ISO_LOCAL_DATE.format(today.minusDays(1));
                            var dayBeforeYesterday = DateTimeFormatter.ISO_LOCAL_DATE.format(today.minusDays(2));

                            queryByDurationForStart.setText(dayBeforeYesterday);
                            queryByDurationForEnd.setText(yesterday);
                        }
                    }
                });
    }

    private void hidesAllQueryFields() {
        queryByDay.setVisible(false);
        queryByWeek.setVisible(false);
        queryByMonth.setVisible(false);
        queryByDurationForStart.setVisible(false);
        queryByDurationForEnd.setVisible(false);
    }

    Optional<Pair<LocalDate, LocalDate>> getSelectDuration() {
        return Optional.ofNullable(choiceBox.getSelectionModel())
                .map(SingleSelectionModel::getSelectedItem)
                .flatMap(QueryType::contentOf)
                .flatMap(queryType -> switch (queryType) {
                    case DAY -> Optional.ofNullable(queryByDay.getText())
                            .filter(Predicate.not(String::isBlank))
                            .flatMap(TypeConversionUtil::toDate)
                            .flatMap(this::wrapDurationByDay);

                    case WEEK -> Optional.ofNullable(queryByWeek.getValue())
                            .filter(Predicate.not(String::isBlank))
                            .flatMap(TypeConversionUtil::toInteger)
                            .flatMap(this::wrapDurationByWeek);

                    case MONTH -> Optional.ofNullable(queryByMonth.getValue())
                            .filter(Predicate.not(String::isBlank))
                            .flatMap(TypeConversionUtil::toInteger)
                            .flatMap(this::wrapDurationByMonth);

                    case DURATION -> {
                        Optional<LocalDate> startDateOpt =
                                Optional.ofNullable(queryByDurationForStart.getText())
                                        .filter(Predicate.not(String::isBlank))
                                        .flatMap(TypeConversionUtil::toDate);

                        Optional<LocalDate> endDateOpt =
                                Optional.ofNullable(queryByDurationForEnd.getText())
                                        .filter(Predicate.not(String::isBlank))
                                        .flatMap(TypeConversionUtil::toDate);

                        yield startDateOpt.flatMap(startDate -> endDateOpt.map(endDate -> new Pair<>(startDate, endDate)));
                    }
                });
    }

    private Optional<Pair<LocalDate, LocalDate>> wrapDurationByDay(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(date -> new Pair<>(date, date));
    }

    private Optional<Pair<LocalDate, LocalDate>> wrapDurationByWeek(int week) {
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

        return Optional.of(new Pair<>(weekFirstDay, weekLastDay));
    }

    private Optional<Pair<LocalDate, LocalDate>> wrapDurationByMonth(int month) {
        var maxMonth = 12;
        var minMonth = 1;
        boolean isWrongRange = month < minMonth || month > maxMonth;
        if (isWrongRange) {
            return Optional.empty();
        }

        LocalDate monthDate = LocalDate.now().withMonth(month);
        var monthFirstDay = monthDate.with(TemporalAdjusters.firstDayOfMonth());
        var monthLastDay = monthDate.with(TemporalAdjusters.lastDayOfMonth());

        return Optional.of(new Pair<>(monthFirstDay, monthLastDay));
    }


    static class Pair<A, B> {

        private final A value0;
        private final B value1;

        Pair(A value0, B value1) {
            this.value0 = value0;
            this.value1 = value1;
        }

        public A getValue0() {
            return value0;
        }

        public B getValue1() {
            return value1;
        }
    }


    enum QueryType {
        DAY("天"),
        WEEK("周"),
        MONTH("月"),
        DURATION("范围");

        private final String content;

        QueryType(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public static Optional<QueryType> contentOf(String content) {
            for (QueryType queryType : values()) {
                if (queryType.content.equals(content)) {
                    return Optional.of(queryType);
                }
            }
            return Optional.empty();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> contents = Stream.of(QueryType.values())
                .map(QueryType::getContent)
                .toList();
        choiceBox.setItems(FXCollections.observableArrayList(contents));

        hidesAllQueryFields();

        queryByWeek.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, 53).mapToObj(String::valueOf).toList()));
        queryByWeek.setEditable(true);

        queryByMonth.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, 12).mapToObj(String::valueOf).toList()));
        queryByMonth.setEditable(true);

    }
}

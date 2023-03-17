package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.controller.controller.report.vo.ReportTableVo;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayStatisticsDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ReportTableController implements Initializable {

    private final ReportController reportController;
    private final TodayStrategy todayStrategy;

    public ReportTableController(ReportController reportController, TodayStrategy todayStrategy) {
        this.todayStrategy = todayStrategy;
        this.reportController = reportController;
    }

    @FXML
    private TableView<ReportTableVo> reportTable;
    @FXML
    private TableColumn<ReportTableVo, String> reportTableContent;
    @FXML
    private TableColumn<ReportTableVo, Integer> reportTableClocks;
    @FXML
    private TableColumn<ReportTableVo, Integer> reportTableTime;

    @FXML
    private Label totalClocksLabel;
    @FXML
    private Label totalClocksTime;

    @FXML
    protected void onReportTableQuery() {
        Optional.ofNullable(reportController.choiceBox.getSelectionModel())
                .map(SingleSelectionModel::getSelectedItem)
                .flatMap(ReportController.QueryType::contentOf)
                .ifPresent(queryType -> {
                    Optional<TodayStatisticsDto> statisticsOpt = switch (queryType) {
                        case DAY -> Optional.ofNullable(reportController.queryByDay.getText())
                                .filter(Predicate.not(String::isBlank))
                                .flatMap(this::dateFrom)
                                .flatMap(todayStrategy::getByDay);

                        case WEEK -> Optional.ofNullable(reportController.queryByWeek.getValue())
                                .filter(Predicate.not(String::isBlank))
                                .flatMap(this::intFrom)
                                .flatMap(todayStrategy::getByWeek);

                        case MONTH -> Optional.ofNullable(reportController.queryByMonth.getValue())
                                .filter(Predicate.not(String::isBlank))
                                .flatMap(this::intFrom)
                                .flatMap(todayStrategy::getByMonth);

                        case DURATION -> {
                            Optional<LocalDate> startDateOpt =
                                    Optional.ofNullable(reportController.queryByDurationForStart.getText())
                                            .filter(Predicate.not(String::isBlank))
                                            .flatMap(this::dateFrom);

                            Optional<LocalDate> endDateOpt =
                                    Optional.ofNullable(reportController.queryByDurationForEnd.getText())
                                            .filter(Predicate.not(String::isBlank))
                                            .flatMap(this::dateFrom);

                            yield startDateOpt.flatMap(startDate ->
                                    endDateOpt.flatMap(endDate ->
                                            todayStrategy.getByDuration(startDate, endDate)));
                        }

                    };

                    statisticsOpt.ifPresent(statistics -> {
                        setReportTableData(statistics);
                        totalClocksLabel.setText(String.valueOf(statistics.totalClocks()));
                        totalClocksTime.setText(String.valueOf(statistics.totalTime()));
                    });

                    if (statisticsOpt.isEmpty()) {
                        showEmptyTable();
                    }
                });
    }

    private void setReportTableData(TodayStatisticsDto statistics) {
        var reportTableData = statistics.todayList()
                .stream()
                .filter(base -> base.clocks() != 0)
                .map(base -> new ReportTableVo(base.content(), base.clocks(), base.time()))
                .sorted(Comparator.comparing(ReportTableVo::clocks).reversed().thenComparing(ReportTableVo::content))
                .toList();
        reportTable.setItems(FXCollections.observableArrayList(reportTableData));
    }

    private void showEmptyTable() {
        reportTable.setItems(FXCollections.observableArrayList(Collections.emptyList()));
        totalClocksLabel.setText("");
        totalClocksTime.setText("");
    }


    private Optional<LocalDate> dateFrom(String dateStr) {
        try {
            DateTimeFormatter isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate date = LocalDate.from(isoLocalDate.parse(dateStr));
            return Optional.of(date);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<Integer> intFrom(String intStr) {
        try {
            return Optional.of(Integer.parseInt(intStr));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportTableContent.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().content()));
        reportTableClocks.setCellValueFactory(column -> new SimpleIntegerProperty(column.getValue().clocks()).asObject());
        reportTableTime.setCellValueFactory(column -> new SimpleIntegerProperty(column.getValue().time()).asObject());
    }
}

package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.controller.controller.report.vo.ReportTableVo;
import com.tool.pomodoro.technique.tool.controller.util.TypeConversionUtil;
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

    public ReportTableController(ReportController reportController) {
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
        Optional<TodayStatisticsDto> statisticsOpt = reportController.getStatistics();

        statisticsOpt.ifPresent(statistics -> {
            setReportTableData(statistics);
            totalClocksLabel.setText(String.valueOf(statistics.totalClocks()));
            totalClocksTime.setText(String.valueOf(statistics.totalTime()));
        });

        if (statisticsOpt.isEmpty()) {
            showEmptyTable();
        }
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportTableContent.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().content()));
        reportTableClocks.setCellValueFactory(column -> new SimpleIntegerProperty(column.getValue().clocks()).asObject());
        reportTableTime.setCellValueFactory(column -> new SimpleIntegerProperty(column.getValue().time()).asObject());
    }
}

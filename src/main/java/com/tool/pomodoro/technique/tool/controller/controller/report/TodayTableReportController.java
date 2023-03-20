package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.controller.controller.report.vo.ReportTableVo;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayReportStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayTableReportRecordDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class TodayTableReportController implements Initializable {

    private final ReportController reportController;
    private final TodayReportStrategy todayReportStrategy;

    public TodayTableReportController(ReportController reportController, TodayReportStrategy todayReportStrategy) {
        this.reportController = reportController;
        this.todayReportStrategy = todayReportStrategy;
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
        var tableValueOpt = reportController.getSelectDuration()
                .flatMap(pair -> todayReportStrategy.table(pair.getValue0(), pair.getValue1()));


        tableValueOpt.ifPresent(tableValue -> {
            setReportTableData(tableValue.table());
            totalClocksLabel.setText(String.valueOf(tableValue.totalClocks()));
            totalClocksTime.setText(String.valueOf(tableValue.totalTime()));
        });

        if (tableValueOpt.isEmpty()) {
            showEmptyTable();
        }
    }

    private void setReportTableData(List<TodayTableReportRecordDto> table) {
        var reportTableData = table
                .stream()
                .filter(item -> item.clocks() != 0)
                .map(item -> new ReportTableVo(item.content(), item.clocks(), item.time()))
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

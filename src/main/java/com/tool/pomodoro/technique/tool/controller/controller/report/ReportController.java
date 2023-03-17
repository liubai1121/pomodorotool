package com.tool.pomodoro.technique.tool.controller.controller.report;

import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ReportController implements Initializable {

    private final StrategyFactory strategyFactory;

    public ReportController(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
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
                            queryByWeek.setValue(String.valueOf(defaultWeek));
                        }

                        case MONTH -> {
                            queryByMonth.setVisible(true);
                            var month = LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
                            var defaultMonth = month - 1 < 1 ? month : month - 1;
                            queryByMonth.setValue(String.valueOf(defaultMonth));
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

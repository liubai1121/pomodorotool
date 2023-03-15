package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Predicate;

public class TodayAddController {

    private final TodayStrategy todayStrategy;

    public TodayAddController(TodayStrategy todayStrategy) {
        this.todayStrategy = todayStrategy;
    }

    @FXML
    private TextField todayContent;
    @FXML
    private TextField todayClocks;

    @FXML
    protected void onTodayAdd() {
        Optional.ofNullable(todayContent.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(content -> Optional.ofNullable(todayClocks.getText())
                        .filter(Predicate.not(String::isBlank))
                        .map(Integer::parseInt)
                        .ifPresent(clocks -> {
                            var addDto = new TodayAddDto(content, clocks);
                            todayStrategy.add(addDto);

                            Stage stage = (Stage) todayContent.getScene().getWindow();
                            WindowUtil.close(stage);
                        }));
    }
}

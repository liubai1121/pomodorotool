package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class TodayEditController implements Initializable {

    private final TodayStrategy todayStrategy;
    private final TodayVo today;

    public TodayEditController(TodayStrategy todayStrategy, TodayVo today) {
        this.todayStrategy = todayStrategy;
        this.today = today;
    }

    @FXML
    private Label todayId;
    @FXML
    private TextField todayContent;
    @FXML
    private TextField todayClocks;
    @FXML
    private Label todayCreateTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayId.setText(today.id());
        todayContent.setText(today.content());
        todayClocks.setText(String.valueOf(today.clocks()));
        todayCreateTime.setText(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(today.createTime()));
    }

    @FXML
    protected void onTodayEdit() {
        Optional.ofNullable(todayId.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> Optional.ofNullable(todayContent.getText())
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(content -> Optional.ofNullable(todayClocks.getText())
                                .filter(Predicate.not(String::isBlank))
                                .map(Integer::parseInt)
                                .ifPresent(clocks -> {
                                    var updateDto = new TodayUpdateDto(id, content, clocks);
                                    todayStrategy.update(updateDto);

                                    Stage stage = (Stage) todayId.getScene().getWindow();
                                    WindowUtil.close(stage);
                                }))
                );
    }
}

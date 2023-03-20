package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.common.command.CompositeCommand;
import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.CloseWindowCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.CreateTodayCountdownRemindWindowCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.TodayIncrementClockCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.TodayCountdownCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class TodayCountdownController {

    private final TodayStrategy todayStrategy;
    private final TodayVo todayVo;
    private TodayCountdownCommand labelCountdownCommand;

    public TodayCountdownController(TodayStrategy todayStrategy, TodayVo todayVo) {
        this.todayStrategy = todayStrategy;
        this.todayVo = todayVo;
    }

    private static final String COUNTDOWN_TIME = "00:25:00";

    @FXML
    private Label countdownLabel;

    public void init() {
        Optional.ofNullable(countdownLabel.getScene())
                .map(scene -> (Stage) scene.getWindow())
                .ifPresent(stage -> {
                    countdownLabel.setText(COUNTDOWN_TIME);

                    var closeWindowCommand = new CloseWindowCommand(stage);
                    var remindCommand = new CreateTodayCountdownRemindWindowCommand();
                    var incrementClockCommand = new TodayIncrementClockCommand(todayStrategy, todayVo.id());
                    var compositeCommand = new CompositeCommand(List.of(incrementClockCommand, remindCommand, closeWindowCommand));

                    labelCountdownCommand = new TodayCountdownCommand(countdownLabel, compositeCommand);
                    PerSecondCommandScheduleQueue.getInstance().put(labelCountdownCommand);
                });
    }

    public void cancel() {
        Optional.ofNullable(labelCountdownCommand)
                .ifPresent(TodayCountdownCommand::cancel);
    }
}

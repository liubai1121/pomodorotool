package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.common.command.CompositeCommand;
import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.CloseWindowCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.CreateTodayCountdownRemindWindowCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.TodayCountdownCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.command.TodayIncrementClockCommand;
import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.util.TypeConversionUtil;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class TodayCountdownController implements Initializable {
    private final TodayStrategy todayStrategy;
    private final TodayVo todayVo;
    private TodayCountdownCommand labelCountdownCommand;

    public TodayCountdownController(TodayStrategy todayStrategy, TodayVo todayVo) {
        this.todayStrategy = todayStrategy;
        this.todayVo = todayVo;
    }

    public void cancel() {
        Optional.ofNullable(labelCountdownCommand)
                .ifPresent(TodayCountdownCommand::cancel);
    }

    @FXML
    public void start() {
        Integer hours = Optional.ofNullable(countdownHours.getValue())
                .flatMap(TypeConversionUtil::toInteger)
                .filter(hour -> hour > 0)
                .orElse(0);
        Integer minutes = Optional.ofNullable(countdownMinutes.getValue())
                .flatMap(TypeConversionUtil::toInteger)
                .filter(minute -> minute > 0)
                .orElse(0);
        Integer seconds = Optional.ofNullable(countdownSeconds.getValue())
                .flatMap(TypeConversionUtil::toInteger)
                .filter(second -> second > 0)
                .orElse(0);

        var countdownTime = LocalTime.of(hours, minutes, seconds);

        boolean isUnavailableCountdownTime = countdownTime.toSecondOfDay() == 0;
        if (isUnavailableCountdownTime) {
            return;
        }

        HideSettingsComponent();
        startCountdown(countdownTime);
    }

    private void HideSettingsComponent() {
        countdownHours.setVisible(false);
        countdownMinutes.setVisible(false);
        countdownSeconds.setVisible(false);
        hoursMinutesDelimiter.setVisible(false);
        minutesSecondsDelimiter.setVisible(false);
        countdownStartButton.setVisible(false);
    }

    private void startCountdown(LocalTime countdownTime) {
        Optional.ofNullable(countdownLabel.getScene())
                .map(scene -> (Stage) scene.getWindow())
                .ifPresent(stage -> {
                    var closeWindowCommand = new CloseWindowCommand(stage);
                    var remindCommand = new CreateTodayCountdownRemindWindowCommand();
                    var incrementClockCommand = new TodayIncrementClockCommand(todayStrategy, todayVo.id());
                    var compositeCommand = new CompositeCommand(List.of(incrementClockCommand, remindCommand, closeWindowCommand));

                    labelCountdownCommand = new TodayCountdownCommand(countdownLabel, countdownTime, compositeCommand);
                    PerSecondCommandScheduleQueue.getInstance().put(labelCountdownCommand);
                });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countdownHours.setItems(FXCollections.observableArrayList(IntStream.range(0, 100).mapToObj(num -> String.format("%02d", num)).toList()));
        countdownMinutes.setItems(FXCollections.observableArrayList(IntStream.range(0, 60).mapToObj(num -> String.format("%02d", num)).toList()));
        countdownSeconds.setItems(FXCollections.observableArrayList(IntStream.range(0, 60).mapToObj(num -> String.format("%02d", num)).toList()));

        countdownHours.setEditable(true);
        countdownMinutes.setEditable(true);
        countdownSeconds.setEditable(true);

        countdownHours.getSelectionModel().select(0);
        countdownMinutes.getSelectionModel().select(25);
        countdownSeconds.getSelectionModel().select(0);

    }

    @FXML
    private Label countdownLabel;

    @FXML
    private ComboBox<String> countdownHours;
    @FXML
    private ComboBox<String> countdownMinutes;
    @FXML
    private ComboBox<String> countdownSeconds;
    @FXML
    private Label hoursMinutesDelimiter;
    @FXML
    private Label minutesSecondsDelimiter;
    @FXML
    private Button countdownStartButton;
}

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class TodayCountdownController implements Initializable {
    private final TodayStrategy todayStrategy;
    private final TodayVo todayVo;
    private TodayCountdownCommand labelCountdownCommand;

    private Status status;

    enum Status {
        START,
        STOP;
    }

    public TodayCountdownController(TodayStrategy todayStrategy, TodayVo todayVo) {
        this.todayStrategy = todayStrategy;
        this.todayVo = todayVo;
    }

    public void cancel() {
        Optional.ofNullable(labelCountdownCommand)
                .ifPresent(TodayCountdownCommand::stop);
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

        setStatus(Status.START);
        startCountdown(countdownTime);
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
                    startCountdown();
                });
    }

    private Status pauseStatus;

    @FXML
    protected void pause() {
        switch (this.pauseStatus) {
            case START -> {
                stopCountdown();
                this.pauseStatus = Status.STOP;
                countdownPauseButton.setText("继续");
            }
            case STOP -> {
                startCountdown();
                this.pauseStatus = Status.START;
                countdownPauseButton.setText("暂停");
            }
        }
    }

    @FXML
    protected void complete() {
        labelCountdownCommand.complete();
    }

    @FXML
    protected void reset() {
        labelCountdownCommand.stop();
        labelCountdownCommand = null;
        setStatus(Status.STOP);
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (status.equals(Status.STOP)) {
                start();
            }
        }
    }


    private void stopCountdown() {
        labelCountdownCommand.stop();
    }

    private void startCountdown() {
        labelCountdownCommand.start();
        PerSecondCommandScheduleQueue.getInstance().put(labelCountdownCommand);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setStatus(Status.STOP);
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

    private void setStatus(Status status) {
        if (Objects.isNull(status) || status.equals(this.status)) {
            return;
        }
        this.status = status;
        switch (status) {
            case START -> {
                hideSettingsComponent();
                showCountdownComponent();
                pauseStatus = status;
            }
            case STOP -> {
                showSettingsComponent();
                hideCountdownComponent();
                pauseStatus = status;
            }
        }
    }

    private void hideSettingsComponent() {
        countdownHours.setVisible(false);
        countdownMinutes.setVisible(false);
        countdownSeconds.setVisible(false);
        hoursMinutesDelimiter.setVisible(false);
        minutesSecondsDelimiter.setVisible(false);
        countdownStartButton.setVisible(false);
    }

    private void showSettingsComponent() {
        countdownHours.setVisible(true);
        countdownMinutes.setVisible(true);
        countdownSeconds.setVisible(true);
        hoursMinutesDelimiter.setVisible(true);
        minutesSecondsDelimiter.setVisible(true);
        countdownStartButton.setVisible(true);
    }

    private void hideCountdownComponent() {
        countdownLabel.setVisible(false);
        countdownResetButton.setVisible(false);
        countdownPauseButton.setVisible(false);
        countdownCompleteButton.setVisible(false);
    }

    private void showCountdownComponent() {
        countdownLabel.setVisible(true);
        countdownResetButton.setVisible(true);
        countdownPauseButton.setVisible(true);
        countdownCompleteButton.setVisible(true);
    }


    @FXML
    private Label countdownLabel;
    @FXML
    private Button countdownResetButton;
    @FXML
    private Button countdownPauseButton;
    @FXML
    private Button countdownCompleteButton;

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

package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.today.TodayStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.todotodaymove.TodoTodayMoveStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TodayController implements Initializable {

    private final TodayStrategy todayStrategy = TodayStrategyFactory.create();
    private final TodoTodayMoveStrategy moveStrategy = TodoTodayMoveStrategyFactory.create();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayContentTableColumn.setCellValueFactory(contentColumn -> new SimpleStringProperty(contentColumn.getValue().content()));
        todayClocksTableColumn.setCellValueFactory(clocksColumn -> new SimpleIntegerProperty(clocksColumn.getValue().clocks()).asObject());

        refreshTodayTableView();
    }

    @FXML
    private TableView<TodayVo> todayTableView;
    @FXML
    private TableColumn<TodayVo, String> todayContentTableColumn;
    @FXML
    private TableColumn<TodayVo, Integer> todayClocksTableColumn;

    @FXML
    private TextField addTodayField;

    @FXML
    protected void onAddTodayButtonClick() {
        Optional.ofNullable(addTodayField.getCharacters())
                .map(CharSequence::toString)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(this::addToday);

        addTodayField.clear();
        refreshTodayTableView();
    }

    private void addToday(String content) {
        var dto = new TodayAddDto(content);
        todayStrategy.add(dto);
    }

    @FXML
    protected void onTodayCopyToTodoMenuItem() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> moveStrategy.copyTodayToTodo(todayVo.id()));
    }

    @FXML
    protected void onTodayCutToTodoMenuItem() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> {
                    moveStrategy.cutTodayToTodo(todayVo.id());
                    refreshTodayTableView();
                });
    }

    @FXML
    protected void onTodayDeleteMenuItem() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> {
                    todayStrategy.delete(todayVo.id());
                    refreshTodayTableView();
                });
    }

    @FXML
    protected void onStartClock() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(this::createCountdownWindow);
    }

    private void createCountdownWindow(TodayVo todayVo) {
        TodayCountdownController todayCountdownController = new TodayCountdownController(todayVo);
        Stage stage = WindowUtil.create(todayVo.content(), "today/today-countdown.fxml", todayCountdownController);

        stage.setOnShowing(event -> todayCountdownController.init());

        stage.setOnCloseRequest(event -> {
            refreshTodayTableView();
        });

        stage.show();
    }

    private void refreshTodayTableView() {
        todayStrategy.all()
                .map(this::wrapTodayVoList)
                .map(FXCollections::observableList)
                .ifPresent(todayTableView::setItems);
    }

    private List<TodayVo> wrapTodayVoList(List<TodayDto> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(item -> new TodayVo(item.id(), item.content(), item.clocks()))
                .collect(Collectors.toList());
    }


    @FXML
    protected void onRowSelected() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> ToolController.getController(TodayDetailController.class)
                        .ifPresent(controller -> controller.display(todayVo)));
    }
}

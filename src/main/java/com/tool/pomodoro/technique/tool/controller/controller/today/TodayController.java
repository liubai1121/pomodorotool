package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.controller.todo.TodoController;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
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

    private final TodayStrategy todayStrategy;
    private final TodoTodayMoveStrategy moveStrategy;

    public TodayController(StrategyFactory strategyFactory) {
        this.todayStrategy = strategyFactory.createTodayStrategy();
        this.moveStrategy = strategyFactory.createTodoTodayMoveStrategy();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayContentTableColumn.setCellValueFactory(contentColumn -> new SimpleStringProperty(contentColumn.getValue().content()));
        todayClocksTableColumn.setCellValueFactory(clocksColumn -> new SimpleIntegerProperty(clocksColumn.getValue().clocks()).asObject());

        refreshTableView();
        ToolController.registerController(this);
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
        refreshTableView();
    }

    private void addToday(String content) {
        var dto = new TodayAddDto(content);
        todayStrategy.add(dto);
    }

    @FXML
    protected void onCopyToTodo() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> {
                    moveStrategy.copyTodayToTodo(todayVo.id());
                    ToolController.getController(TodoController.class)
                            .ifPresent(TodoController::refreshTableView);
                });
    }

    @FXML
    protected void onCutToTodo() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> {
                    moveStrategy.cutTodayToTodo(todayVo.id());
                    refreshTableView();
                    ToolController.getController(TodoController.class)
                            .ifPresent(TodoController::refreshTableView);
                });
    }

    @FXML
    protected void onAdd() {
        var addController = new TodayAddController(todayStrategy);
        var stage = WindowUtil.create("新增", "today/today-add.fxml", addController);
        stage.setAlwaysOnTop(true);
        stage.show();

        stage.setOnCloseRequest(event -> {
            refreshTableView();
        });
    }

    @FXML
    protected void onEdit() {
        getSelectedLabel()
                .ifPresent(today -> {
                    var editController = new TodayEditController(todayStrategy, today);
                    var stage = WindowUtil.create("修改", "today/today-edit.fxml", editController);
                    stage.setAlwaysOnTop(true);
                    stage.show();

                    stage.setOnCloseRequest(event -> {
                        refreshTableView();
                    });
                });
    }

    private Optional<TodayVo> getSelectedLabel() {
        return Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem);
    }

    @FXML
    protected void onDelete() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> {
                    todayStrategy.delete(todayVo.id());
                    refreshTableView();
                });
    }

    @FXML
    protected void onStartClock() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(this::createCountdownWindow);
    }

    private void createCountdownWindow(TodayVo todayVo) {
        TodayCountdownController todayCountdownController = new TodayCountdownController(todayStrategy, todayVo);
        Stage stage = WindowUtil.create(todayVo.content(), "today/today-countdown.fxml", todayCountdownController);

        stage.setOnShowing(event -> todayCountdownController.init());

        stage.setOnCloseRequest(event -> {
            todayCountdownController.cancel();
            refreshTableView();
        });

        stage.show();
    }

    public void refreshTableView() {
        todayStrategy.all()
                .map(this::wrapTodayVoList)
                .map(FXCollections::observableList)
                .ifPresent(todayTableView::setItems);
    }

    private List<TodayVo> wrapTodayVoList(List<TodayDto> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(item -> new TodayVo(item.id(), item.content(), item.clocks(), item.createTime()))
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

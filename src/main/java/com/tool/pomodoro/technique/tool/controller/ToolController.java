package com.tool.pomodoro.technique.tool.controller;

import com.tool.pomodoro.technique.tool.common.queue.command.CompositeCommand;
import com.tool.pomodoro.technique.tool.common.queue.PerSecondCommandQueue;
import com.tool.pomodoro.technique.tool.controller.command.CloseWindowCommand;
import com.tool.pomodoro.technique.tool.controller.command.IncrementClockCommand;
import com.tool.pomodoro.technique.tool.controller.command.LabelCountdownCommand;
import com.tool.pomodoro.technique.tool.controller.command.CreateRemindWindowCommand;
import com.tool.pomodoro.technique.tool.controller.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.vo.TodoVo;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ToolController {

    private final TodoStrategy todoStrategy;
    private final TodayStrategy todayStrategy;
    private final TodoTodayMoveStrategy moveStrategy;

    public ToolController(TodoStrategy todoStrategy, TodayStrategy todayStrategy, TodoTodayMoveStrategy moveStrategy) {
        this.todoStrategy = todoStrategy;
        this.todayStrategy = todayStrategy;
        this.moveStrategy = moveStrategy;
    }

    @FXML
    private TableView<TodoVo> todoTableView;
    @FXML
    private TableColumn<TodoVo, String> todoContentTableColumn;

    @FXML
    protected void onLoadTodo() {
        todoStrategy.all()
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .map(item -> new TodoVo(item.getId(), item.getContent()))
                        .collect(Collectors.toList()))
                .map(FXCollections::observableList)
                .ifPresent(this::setTodoTableView);
    }

    private void setTodoTableView(ObservableList<TodoVo> list) {
        // 设置tableView的列可更改
        todoTableView.setEditable(true);
        // 设置列的编辑方式
        todoContentTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // 设置列的取值
        todoContentTableColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        // 设置列的编辑提交函数
        todoContentTableColumn.setOnEditCommit(event -> {
            TablePosition<TodoVo, String> tablePosition = event.getTablePosition();
            TableView<TodoVo> tableView = tablePosition.getTableView();
            ObservableList<TodoVo> items = tableView.getItems();
            TodoVo todoVo = items.get(tablePosition.getRow());
            todoVo.setContent(event.getNewValue());
            updateTodo(todoVo);
        });

        todoTableView.setItems(list);
    }

    private void updateTodo(TodoVo vo) {
        var updateTodo = new TodoUpdateDto();
        updateTodo.setId(vo.getId());
        updateTodo.setContent(vo.getContent());
        todoStrategy.update(updateTodo);
    }

    @FXML
    private TextField addTodoField;

    @FXML
    protected void onAddTodoButtonClick() {
        Optional.ofNullable(addTodoField.getCharacters())
                .map(CharSequence::toString)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(this::addTodo);

        addTodoField.clear();
        onLoadTodo();
    }

    private void addTodo(String todo) {
        var dto = new TodoAddDto();
        dto.setTodo(todo);
        todoStrategy.add(dto);
    }

    @FXML
    private ContextMenu todoContextMenu;
    @FXML
    private MenuItem todoDeleteMenuItem;

    @FXML
    protected void onTodoDeleteMenuItem() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .flatMap(selectionModel -> Optional.of(selectionModel.getSelectedIndex())
                        .filter(selectedIndex -> selectedIndex >= 0))
                .ifPresent(selectedIndex -> Optional.ofNullable(todoTableView.getItems())
                        .filter(Predicate.not(Collection::isEmpty))
                        .filter(items -> items.size() > selectedIndex)
                        .ifPresent(items -> {
                            TodoVo removedVO = items.remove(selectedIndex.intValue());
                            todoStrategy.delete(removedVO.getId());
                        }));
    }

    @FXML
    private MenuItem todoCopyToTodayMenuItem;

    @FXML
    protected void onTodoCopyToTodayMenuItem() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todoVo -> moveStrategy.copyTodoToToday(todoVo.getId()));
    }

    @FXML
    private MenuItem todoCutToTodayMenuItem;

    @FXML
    protected void onTodoCutToTodayMenuItem() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .flatMap(selectionModel -> Optional.of(selectionModel.getSelectedIndex())
                        .filter(selectedIndex -> selectedIndex >= 0))
                .ifPresent(selectedIndex -> Optional.ofNullable(todoTableView.getItems())
                        .filter(Predicate.not(Collection::isEmpty))
                        .filter(items -> items.size() > selectedIndex)
                        .ifPresent(items -> {
                            TodoVo removedVO = items.remove(selectedIndex.intValue());
                            moveStrategy.cutTodoToToday(removedVO.getId());
                        }));
    }


    @FXML
    private TableView<TodayVo> todayTableView;
    @FXML
    private TableColumn<TodayVo, String> todayContentTableColumn;
    @FXML
    private TableColumn<TodayVo, Integer> todayClocksTableColumn;

    @FXML
    protected void onLoadToday() {
        todayStrategy.all()
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .map(item -> new TodayVo(item.getId(), item.getContent(), item.getClocks()))
                        .collect(Collectors.toList()))
                .map(FXCollections::observableList)
                .ifPresent(this::setTodayTableView);
    }

    private void setTodayTableView(ObservableList<TodayVo> list) {
        // 设置tableView的列可更改
        todayTableView.setEditable(true);
        // 设置列的编辑方式
        todayContentTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // 设置列的取值
        todayContentTableColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        // 设置列的编辑提交函数
        todayContentTableColumn.setOnEditCommit(event -> {
            TablePosition<TodayVo, String> tablePosition = event.getTablePosition();
            TableView<TodayVo> tableView = tablePosition.getTableView();
            ObservableList<TodayVo> items = tableView.getItems();
            TodayVo todayVO = items.get(tablePosition.getRow());
            todayVO.setContent(event.getNewValue());
            updateToday(todayVO);
        });


        // 设置列的编辑方式
        todayClocksTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Integer object) {
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                return Integer.valueOf(string);
            }
        }));
        // 设置列的取值
        todayClocksTableColumn.setCellValueFactory(new PropertyValueFactory<>("clocks"));
        // 设置列的编辑提交函数
        todayClocksTableColumn.setOnEditCommit(event -> {
            TablePosition<TodayVo, Integer> tablePosition = event.getTablePosition();
            TableView<TodayVo> tableView = tablePosition.getTableView();
            ObservableList<TodayVo> items = tableView.getItems();
            TodayVo todayVO = items.get(tablePosition.getRow());
            todayVO.setClocks(event.getNewValue());
            updateToday(todayVO);
        });

        todayTableView.setItems(list);
    }

    private void updateToday(TodayVo vo) {
        var updateToday = new TodayUpdateDto();
        updateToday.setId(vo.getId());
        updateToday.setContent(vo.getContent());
        updateToday.setClocks(vo.getClocks());
        todayStrategy.update(updateToday);
    }

    @FXML
    private TextField addTodayField;

    @FXML
    protected void onAddTodayButtonClick() {
        Optional.ofNullable(addTodayField.getCharacters())
                .map(CharSequence::toString)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(this::addToday);

        addTodayField.clear();
        onLoadToday();
    }

    private void addToday(String content) {
        var dto = new TodayAddDto();
        dto.setContent(content);
        todayStrategy.add(dto);
    }

    @FXML
    protected void onTodayDeleteMenuItem() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .flatMap(selectionModel -> Optional.of(selectionModel.getSelectedIndex())
                        .filter(selectedIndex -> selectedIndex >= 0))
                .ifPresent(selectedIndex -> Optional.ofNullable(todayTableView.getItems())
                        .filter(Predicate.not(Collection::isEmpty))
                        .filter(items -> items.size() > selectedIndex)
                        .ifPresent(items -> {
                            TodayVo removedVO = items.remove(selectedIndex.intValue());
                            todayStrategy.delete(removedVO.getId());
                        }));
    }

    @FXML
    protected void onTodayCopyToTodoMenuItem() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todayVo -> moveStrategy.copyTodayToTodo(todayVo.getId()));
    }

    @FXML
    protected void onTodayCutToTodoMenuItem() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .flatMap(selectionModel -> Optional.of(selectionModel.getSelectedIndex())
                        .filter(selectedIndex -> selectedIndex >= 0))
                .ifPresent(selectedIndex -> Optional.ofNullable(todayTableView.getItems())
                        .filter(Predicate.not(Collection::isEmpty))
                        .filter(items -> items.size() > selectedIndex)
                        .ifPresent(items -> {
                            TodayVo removedVO = items.remove(selectedIndex.intValue());
                            moveStrategy.cutTodayToTodo(removedVO.getId());
                        }));
    }

    @FXML
    protected void onStartClock() {
        Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(this::createCountdownWindow);
    }

    private void createCountdownWindow(TodayVo todayVo) {
        final var countdownDefaultTime = "00:30:00";

        Label countdownLabel = new Label(countdownDefaultTime);
        countdownLabel.setPrefHeight(61.0);
        countdownLabel.setPrefWidth(200.0);
        countdownLabel.setLayoutX(120.0);
        countdownLabel.setLayoutY(60.0);
        countdownLabel.setFont(new Font(48.0));

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(181.0);
        anchorPane.setPrefWidth(440.0);
        anchorPane.getChildren().add(countdownLabel);
        Stage stage = new Stage();

        stage.setTitle(todayVo.getContent());
        stage.setScene(new Scene(anchorPane));
        stage.show();

        stage.setOnCloseRequest(event -> {
            onLoadToday();
        });

        var closeWindowCommand = new CloseWindowCommand(stage);
        var remindCommand = new CreateRemindWindowCommand();
        var incrementClockCommand = new IncrementClockCommand(todayVo.getId());
        var compositeCommand = new CompositeCommand(List.of(incrementClockCommand, remindCommand, closeWindowCommand));

        var labelCountdownCommand = new LabelCountdownCommand(countdownLabel, compositeCommand);
        PerSecondCommandQueue.getInstance().put(labelCountdownCommand);
    }
}
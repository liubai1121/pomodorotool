package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.tool.pomodoro.technique.tool.controller.controller.today.TodayController;
import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;
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

public class TodoController implements Initializable {

    private final TodoStrategy todoStrategy;
    private final TodoTodayMoveStrategy moveStrategy;

    public TodoController(StrategyFactory strategyFactory) {
        this.todoStrategy = strategyFactory.createTodoStrategy();
        this.moveStrategy = strategyFactory.createTodoTodayMoveStrategy();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todoContentTableColumn.setCellValueFactory(contentColumn -> new SimpleStringProperty(contentColumn.getValue().content()));

        refreshTableView();
        ToolController.registerController(this);
    }

    @FXML
    private TableView<TodoVo> todoTableView;
    @FXML
    private TableColumn<TodoVo, String> todoContentTableColumn;

    @FXML
    private TextField addTodoField;

    @FXML
    protected void onAddTodoButtonClick() {
        Optional.ofNullable(addTodoField.getCharacters())
                .map(CharSequence::toString)
                .filter(Predicate.not(String::isBlank))
                .ifPresent(this::addTodo);

        addTodoField.clear();
        refreshTableView();
    }

    private void addTodo(String todo) {
        var dto = new TodoAddDto(todo);
        todoStrategy.add(dto);
    }

    @FXML
    protected void onCopyToToday() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todoVo -> {
                    moveStrategy.copyTodoToToday(todoVo.id());
                    ToolController.getController(TodayController.class)
                            .ifPresent(TodayController::refreshTableView);
                });
    }

    @FXML
    protected void onCutToToday() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todoVo -> {
                    moveStrategy.cutTodoToToday(todoVo.id());
                    refreshTableView();
                    ToolController.getController(TodayController.class)
                            .ifPresent(TodayController::refreshTableView);
                });
    }

    @FXML
    protected void onAdd() {
        var addController = new TodoAddController(todoStrategy);
        Stage stage = WindowUtil.create("新增", "todo/todo-add.fxml", addController);
        stage.setAlwaysOnTop(true);
        stage.show();

        stage.setOnCloseRequest(event -> {
            refreshTableView();
        });
    }

    @FXML
    protected void onEdit() {
        getSelectedLabel()
                .ifPresent(todo -> {
                    var editController = new TodoEditController(todoStrategy, todo);
                    Stage stage = WindowUtil.create("修改", "todo/todo-edit.fxml", editController);
                    stage.setAlwaysOnTop(true);
                    stage.show();

                    stage.setOnCloseRequest(event -> {
                        refreshTableView();
                    });
                });
    }

    private Optional<TodoVo> getSelectedLabel() {
        return Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem);
    }

    @FXML
    protected void onDelete() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todoVo -> {
                    todoStrategy.delete(todoVo.id());
                    refreshTableView();
                });
    }

    public void refreshTableView() {
        todoStrategy.all()
                .map(this::wrapTodoVoList)
                .map(FXCollections::observableList)
                .ifPresent(todoTableView::setItems);
    }

    private List<TodoVo> wrapTodoVoList(List<TodoDto> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(item -> new TodoVo(item.id(), item.content(), item.createTime()))
                .collect(Collectors.toList());
    }

    @FXML
    protected void onRowSelected() {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todoVo -> ToolController.getController(TodoDetailController.class)
                        .ifPresent(controller -> controller.display(todoVo)));
    }
}

package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.tool.pomodoro.technique.tool.controller.controller.today.TodayController;
import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoCategoryVo;
import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoDto;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.TodoTodayMoveStrategy;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TodoController implements Initializable {

    private final TodoStrategy todoStrategy;
    private final TodoTodayMoveStrategy moveStrategy;
    private final TodoCategoryStrategy todoCategoryStrategy;

    public TodoController(StrategyFactory strategyFactory) {
        this.todoStrategy = strategyFactory.createTodoStrategy();
        this.moveStrategy = strategyFactory.createTodoTodayMoveStrategy();
        this.todoCategoryStrategy = strategyFactory.createTodoCategoryStrategy();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTreeView();

        todoContentTableColumn.setCellValueFactory(contentColumn -> new SimpleStringProperty(contentColumn.getValue().content()));

        refreshCategoryTree();
        ToolController.registerController(this);
    }

    @FXML
    private TreeView<TodoCategoryVo> todoCategoryTreeView;

    private void initTreeView() {
        TreeItem<TodoCategoryVo> roomNode = new TreeItem<>(new TodoCategoryVo("", "类别"));
        roomNode.setExpanded(true);

        todoCategoryTreeView.setCellFactory(treeViewItem -> new TreeCell<>() {
            @Override
            protected void updateItem(TodoCategoryVo item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.name());
                }
            }
        });
        todoCategoryTreeView.setRoot(roomNode);
    }


    @FXML
    private TableView<TodoVo> todoTableView;
    @FXML
    private TableColumn<TodoVo, String> todoContentTableColumn;


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
        getCategorySelected()
                .ifPresent(todoCategory -> {
                    var addController = new TodoAddController(todoStrategy, todoCategory.id());
                    Stage stage = WindowUtil.create("新增", "todo/todo-add.fxml", addController);
                    stage.setAlwaysOnTop(true);
                    stage.show();

                    stage.setOnCloseRequest(event -> {
                        refreshTableView();
                    });
                });
    }

    @FXML
    protected void onEdit() {
        getTableSelected()
                .ifPresent(todo -> {
                    var editController = new TodoEditController(todoStrategy, todo);
                    Stage stage = WindowUtil.create("编辑", "todo/todo-edit.fxml", editController);
                    stage.setAlwaysOnTop(true);
                    stage.show();


                    stage.setOnCloseRequest(event -> {
                        refreshTableView();
                    });
                });
    }

    private Optional<TodoVo> getTableSelected() {
        return Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem);
    }

    @FXML
    protected void onDelete() {
        getTableSelected()
                .ifPresent(todoVo -> {
                    todoStrategy.delete(todoVo.id());
                    refreshTableView();
                });
    }

    public void refreshTableView() {
        getCategorySelected()
                .flatMap(todoCategory -> todoStrategy.listByCategory(todoCategory.id())
                        .map(this::wrapTodoVoList)
                        .map(FXCollections::observableList))
                .ifPresent(todoTableView::setItems);
    }

    private List<TodoVo> wrapTodoVoList(List<TodoDto> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .map(item -> new TodoVo(item.id(), item.content(), item.createTime()))
                .collect(Collectors.toList());
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onCopyToToday();
        }
    }

    @FXML
    protected void onMouseClicked(MouseEvent mouseEvent) {
        Optional.ofNullable(todoTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .ifPresent(todoVo -> ToolController.getController(TodoDetailController.class)
                        .ifPresent(detailController -> detailController.display(todoVo)));

        boolean isDoubleClick = mouseEvent.getClickCount() == 2;
        if (isDoubleClick) {
            onEdit();
        }
    }

    @FXML
    private ContextMenu todoMenu;

    @FXML
    protected void onTodoMenuRequested() {
        ObservableList<MenuItem> items = todoMenu.getItems();

        Optional<TodoVo> selectedItem = getTableSelected();
        Optional<TodoCategoryVo> categorySelected = getCategorySelected();
        if (selectedItem.isPresent()) {
            items.forEach(item -> item.setDisable(false));
        } else {
            if (categorySelected.isPresent()) {
                items.forEach(item -> {
                    item.setDisable(!item.getText().equals("新增"));
                });
            } else {
                items.forEach(item -> item.setDisable(true));
            }
        }
    }

    @FXML
    protected void onCategoryAdd() {
        var categoryAddController = new TodoCategoryAddController(todoCategoryStrategy);
        Stage stage = WindowUtil.create("新增", "todo/todo-category-add.fxml", categoryAddController);
        stage.setAlwaysOnTop(true);
        stage.show();

        stage.setOnCloseRequest(event -> {
            refreshCategoryTree();
        });
    }

    @FXML
    protected void onCategoryEdit() {
        getCategorySelected()
                .ifPresent(todoCategory -> {
                    var editController = new TodoCategoryEditController(todoCategoryStrategy, todoCategory);
                    Stage stage = WindowUtil.create("编辑", "todo/todo-category-edit.fxml", editController);
                    stage.setAlwaysOnTop(true);
                    stage.show();

                    stage.setOnCloseRequest(event -> {
                        refreshCategoryTree();
                    });
                });
    }

    @FXML
    protected void onCategoryDelete() {
        getCategorySelected()
                .ifPresent(todoCategory -> {
                    todoCategoryStrategy.delete(todoCategory.id());
                    refreshCategoryTree();
                });
    }

    @FXML
    protected void onCategorySelected() {
        getCategorySelected()
                .ifPresent(todoCategory -> {
                    refreshTableView();
                });

        if (getCategorySelected().isEmpty()) {
            displayEmptyTableView();
        }
    }

    private void displayEmptyTableView() {
        todoTableView.getItems().clear();
    }

    private Optional<TodoCategoryVo> getCategorySelected() {
        return Optional.ofNullable(todoCategoryTreeView.getSelectionModel())
                .map(SelectionModel::getSelectedItem)
                .map(TreeItem::getValue)
                .filter(todoCategory -> Objects.nonNull(todoCategory.id()) && !todoCategory.id().isBlank());
    }

    private void refreshCategoryTree() {
        todoCategoryStrategy.all()
                .ifPresent(categories -> {
                    var treeItems = categories.stream()
                            .map(category -> new TreeItem<>(new TodoCategoryVo(category.id(), category.name())))
                            .toList();
                    todoCategoryTreeView.getRoot().getChildren().setAll(treeItems);
                });
    }

    @FXML
    private ContextMenu categoryMenu;

    @FXML
    protected void onCategoryMenuRequested() {
        ObservableList<MenuItem> items = categoryMenu.getItems();

        Optional<TodoCategoryVo> selectedItem = getCategorySelected();
        if (selectedItem.isPresent()) {
            items.forEach(item -> item.setDisable(false));
        } else {
            items.forEach(item -> {
                if (!item.getText().equals("新增")) {
                    item.setDisable(true);
                }
            });
        }
    }

}

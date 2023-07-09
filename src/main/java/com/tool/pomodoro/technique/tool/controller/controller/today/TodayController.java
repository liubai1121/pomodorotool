package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class TodayController implements Initializable {

    private final TodayStrategy todayStrategy;
    private final StrategyFactory strategyFactory;

    public TodayController(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
        this.todayStrategy = strategyFactory.createTodayStrategy();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayContentTableColumn.setCellValueFactory(contentColumn -> new SimpleStringProperty(contentColumn.getValue().content()));
        todayClocksTableColumn.setCellValueFactory(clocksColumn -> new SimpleIntegerProperty(clocksColumn.getValue().clocks()).asObject());
        todayCategoryTableColumn.setCellValueFactory(contentColumn -> new SimpleStringProperty(contentColumn.getValue().category()));

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
    private TableColumn<TodayVo, String> todayCategoryTableColumn;


    @FXML
    protected void onAdd() {
        var addController = new TodayAddController(todayStrategy, strategyFactory.createTodoCategoryStrategy());
        var stage = WindowUtil.create("新增", "today/today-add.fxml", addController);
        stage.setAlwaysOnTop(true);
        stage.show();

        stage.setOnCloseRequest(event -> {
            refreshTableView();
        });
    }

    @FXML
    protected void onEdit() {
        getSelectedItem()
                .ifPresent(today -> {
                    var editController = new TodayEditController(todayStrategy, today, strategyFactory.createTodoCategoryStrategy());
                    var stage = WindowUtil.create("编辑", "today/today-edit.fxml", editController);
                    stage.setAlwaysOnTop(true);
                    stage.show();

                    stage.setOnCloseRequest(event -> {
                        refreshTableView();
                    });
                });
    }

    private Optional<TodayVo> getSelectedItem() {
        return Optional.ofNullable(todayTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem);
    }

    @FXML
    protected void onDelete() {
        getSelectedItem()
                .ifPresent(todayVo -> {
                    todayStrategy.delete(todayVo.id());
                    refreshTableView();
                });
    }

    @FXML
    protected void onStartClock() {
        getSelectedItem()
                .ifPresent(this::createCountdownWindow);
    }

    private void createCountdownWindow(TodayVo todayVo) {
        TodayCountdownController todayCountdownController = new TodayCountdownController(todayStrategy, todayVo);
        Stage stage = WindowUtil.create("倒计时 - " + todayVo.content(), "today/today-countdown.fxml", todayCountdownController);

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
                .map(item -> new TodayVo(item.id(), item.content(), item.clocks(), item.category(), item.createTime()))
                .collect(Collectors.toList());
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onStartClock();
        }
    }

    @FXML
    protected void onMouseClicked(MouseEvent mouseEvent) {
        getSelectedItem()
                .ifPresent(todayVo -> ToolController.getController(TodayDetailController.class)
                        .ifPresent(controller -> controller.display(todayVo)));

        boolean isDoubleClick = mouseEvent.getClickCount() == 2;
        if (isDoubleClick) {
            onEdit();
        }
    }

    // menu
    @FXML
    private ContextMenu todayMenu;

    @FXML
    protected void onContextMenuRequested() {
        ObservableList<MenuItem> items = todayMenu.getItems();

        Optional<TodayVo> selectedItem = getSelectedItem();
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

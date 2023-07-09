package com.tool.pomodoro.technique.tool.controller.controller.label;

import com.tool.pomodoro.technique.tool.controller.controller.label.vo.LabelVo;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelDto;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LabelManagementController implements Initializable {

    private final LabelStrategy labelStrategy;

    public LabelManagementController(LabelStrategy labelStrategy) {
        this.labelStrategy = labelStrategy;
    }

    @FXML
    private TextField labelSearchField;

    @FXML
    private TableView<LabelVo> labelTableView;

    @FXML
    private TableColumn<LabelVo, String> labelTableNameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelTableNameColumn.setCellValueFactory(column -> new SimpleStringProperty(column.getValue().labelName()));

        labelStrategy.all()
                .map(this::wrapVoList)
                .map(FXCollections::observableList)
                .ifPresent(labelTableView::setItems);
    }

    @FXML
    protected void onLabelSearch() {
        Optional.ofNullable(labelSearchField.getText())
                .flatMap(labelStrategy::fuzzyQueryByName)
                .map(this::wrapVoList)
                .ifPresent(vos -> Optional.ofNullable(labelTableView.getItems())
                        .ifPresent(observableList -> observableList.setAll(vos)));
    }

    @FXML
    protected void onLabelAdd() {
        var addController = new LabelAddController(labelStrategy);
        var stage = WindowUtil.create("新增标签", "label/label-add.fxml", addController);
        stage.setAlwaysOnTop(true);
        stage.show();
        stage.setOnCloseRequest(event -> {
            refreshTableView();
        });
    }

    @FXML
    protected void onLabelEdit() {
        getSelectedLabel()
                .ifPresent(label -> {
                    var editController = new LabelEditController(labelStrategy, label);
                    var stage = WindowUtil.create("编辑", "label/label-edit.fxml", editController);
                    stage.setAlwaysOnTop(true);
                    stage.show();
                    stage.setOnCloseRequest(event -> {
                        refreshTableView();
                    });

                });
    }

    @FXML
    protected void onLabelDelete() {
        getSelectedLabel()
                .ifPresent(labelVo -> {
                    labelStrategy.delete(labelVo.labelId());
                    refreshTableView();
                });
    }

    private Optional<LabelVo> getSelectedLabel() {
        return Optional.ofNullable(labelTableView.getSelectionModel())
                .map(SelectionModel::getSelectedItem);
    }

    private void refreshTableView() {
        labelStrategy.all()
                .map(this::wrapVoList)
                .ifPresent(vos -> Optional.ofNullable(labelTableView.getItems())
                        .ifPresent(observableList -> observableList.setAll(vos)));
    }

    private List<LabelVo> wrapVoList(List<LabelDto> labels) {
        return labels.stream()
                .map(label -> new LabelVo(label.labelId(), label.labelName()))
                .collect(Collectors.toList());
    }
}

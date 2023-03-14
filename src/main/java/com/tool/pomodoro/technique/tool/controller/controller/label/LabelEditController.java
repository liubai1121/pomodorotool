package com.tool.pomodoro.technique.tool.controller.controller.label;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.controller.controller.label.vo.LabelVo;
import com.tool.pomodoro.technique.tool.factory.label.LabelStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class LabelEditController implements Initializable {

    private final LabelVo updateLabel;

    private final LabelStrategy labelStrategy = LabelStrategyFactory.create();

    public LabelEditController(LabelVo label) {
        this.updateLabel = label;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelEditLabelIdLabel.setText(updateLabel.labelId());

        labelEditLabelNameTextField.setText(updateLabel.labelName());
    }

    @FXML
    private Label labelEditLabelIdLabel;
    @FXML
    private TextField labelEditLabelNameTextField;

    @FXML
    protected void onLabelEdit() {
        Optional.ofNullable(labelEditLabelIdLabel.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(labelId -> Optional.ofNullable(labelEditLabelNameTextField.getText())
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(newLabelName -> {

                            labelStrategy.update(new LabelUpdateDto(labelId, newLabelName));

                            Stage stage = (Stage) labelEditLabelNameTextField.getScene().getWindow();
                            WindowUtil.close(stage);
                        }));
    }
}

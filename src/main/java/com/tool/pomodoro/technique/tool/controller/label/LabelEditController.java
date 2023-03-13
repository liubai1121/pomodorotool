package com.tool.pomodoro.technique.tool.controller.label;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.controller.vo.LabelVo;
import com.tool.pomodoro.technique.tool.factory.label.LabelStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.label.dto.LabelUpdateDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class LabelEditController implements Initializable {

    private LabelVo updateLabel;

    private final LabelStrategy labelStrategy = LabelStrategyFactory.create();

    public LabelEditController(LabelVo label) {
        this.updateLabel = label;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelEditLabelIdTextField.setText(updateLabel.getLabelId());
        labelEditLabelIdTextField.setEditable(false);

        labelEditLabelNameTextField.setText(updateLabel.getLabelName());
    }

    @FXML
    private TextField labelEditLabelIdTextField;
    @FXML
    private TextField labelEditLabelNameTextField;

    @FXML
    protected void onLabelEdit() {
        Optional.ofNullable(labelEditLabelIdTextField.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(labelId -> Optional.ofNullable(labelEditLabelNameTextField.getText())
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(newLabelName -> {

                            LabelUpdateDto updateDto = new LabelUpdateDto();
                            updateDto.setLabelId(labelId);
                            updateDto.setLabelName(newLabelName);
                            labelStrategy.update(updateDto);

                            Stage stage = (Stage) labelEditLabelNameTextField.getScene().getWindow();
                            WindowUtil.close(stage);
                        }));
    }
}

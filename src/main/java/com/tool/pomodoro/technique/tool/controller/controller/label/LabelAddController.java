package com.tool.pomodoro.technique.tool.controller.controller.label;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.label.LabelStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.label.LabelStrategy;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Predicate;

public class LabelAddController {

    private final LabelStrategy labelStrategy = LabelStrategyFactory.create();

    @FXML
    private TextField labelAddTextField;

    @FXML
    protected void onLabelAdd() {
        Optional.ofNullable(labelAddTextField.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(labelName -> {
                    labelStrategy.add(labelName);

                    Stage stage = (Stage)labelAddTextField.getScene().getWindow();
                    WindowUtil.close(stage);
                });
    }
}

package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoCategoryVo;
import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoVo;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoUpdateDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class TodoCategoryEditController implements Initializable {

    private final TodoCategoryStrategy todoCategoryStrategy;

    private final TodoCategoryVo todoCategoryVo;

    public TodoCategoryEditController(TodoCategoryStrategy todoCategoryStrategy, TodoCategoryVo todoCategoryVo) {
        this.todoCategoryStrategy = todoCategoryStrategy;
        this.todoCategoryVo = todoCategoryVo;
    }

    @FXML
    private Label todoCategoryId;
    @FXML
    private TextField todoCategoryContent;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todoCategoryId.setText(todoCategoryVo.id());
        todoCategoryContent.setText(todoCategoryVo.name());
    }

    @FXML
    protected void onTodoCategoryEdit() {
        Optional.ofNullable(todoCategoryId.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> Optional.ofNullable(todoCategoryContent.getText())
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(content -> {
                            todoCategoryStrategy.update(new TodoCategoryUpdateDto(id, content));

                            Stage stage = (Stage) todoCategoryId.getScene().getWindow();
                            WindowUtil.close(stage);
                        }));
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onTodoCategoryEdit();
        }
    }
}

package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.tool.pomodoro.technique.tool.controller.controller.todo.vo.TodoVo;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
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

public class TodoEditController implements Initializable {

    private final TodoStrategy todoStrategy;

    private final TodoVo todo;

    public TodoEditController(TodoStrategy todoStrategy, TodoVo todo) {
        this.todoStrategy = todoStrategy;
        this.todo = todo;
    }

    @FXML
    private Label todoId;
    @FXML
    private TextField todoContent;
    @FXML
    private Label todoCreateTime;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todoId.setText(todo.id());
        todoContent.setText(todo.content());
        todoCreateTime.setText(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(todo.createTime()));
    }

    @FXML
    protected void onTodoEdit() {
        Optional.ofNullable(todoId.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(id -> Optional.ofNullable(todoContent.getText())
                        .filter(Predicate.not(String::isBlank))
                        .ifPresent(content -> {
                            var updateDto = new TodoUpdateDto(id, content);
                            todoStrategy.update(updateDto);

                            Stage stage = (Stage) todoId.getScene().getWindow();
                            WindowUtil.close(stage);
                        }));
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onTodoEdit();
        }
    }
}

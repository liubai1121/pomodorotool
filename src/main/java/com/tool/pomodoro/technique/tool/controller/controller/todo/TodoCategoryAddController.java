package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryAddDto;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Predicate;

public class TodoCategoryAddController {

    private final TodoCategoryStrategy todoCategoryStrategy;

    public TodoCategoryAddController(TodoCategoryStrategy todoCategoryStrategy) {
        this.todoCategoryStrategy = todoCategoryStrategy;
    }

    @FXML
    private TextField todoCategoryContent;

    @FXML
    protected void onTodoCategoryAdd() {
        Optional.ofNullable(todoCategoryContent.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(content -> {
                    var addDto = new TodoCategoryAddDto(content);
                    todoCategoryStrategy.add(addDto);

                    Stage stage = (Stage) todoCategoryContent.getScene().getWindow();
                    WindowUtil.close(stage);
                });
    }
}

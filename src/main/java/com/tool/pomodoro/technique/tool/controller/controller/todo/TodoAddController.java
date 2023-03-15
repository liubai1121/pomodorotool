package com.tool.pomodoro.technique.tool.controller.controller.todo;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.todo.TodoStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoAddDto;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.function.Predicate;

public class TodoAddController {

    private final TodoStrategy todoStrategy = TodoStrategyFactory.create();

    @FXML
    private TextField todoContent;

    @FXML
    protected void onTodoAdd() {
        Optional.ofNullable(todoContent.getText())
                .filter(Predicate.not(String::isBlank))
                .ifPresent(content -> {
                    var addDto = new TodoAddDto(content);
                    todoStrategy.add(addDto);

                    Stage stage = (Stage) todoContent.getScene().getWindow();
                    WindowUtil.close(stage);
                });
    }
}

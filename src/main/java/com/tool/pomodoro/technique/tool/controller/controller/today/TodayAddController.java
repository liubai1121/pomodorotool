package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayAddDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class TodayAddController implements Initializable {

    private final TodayStrategy todayStrategy;
    private final TodoCategoryStrategy todoCategoryStrategy;

    public TodayAddController(TodayStrategy todayStrategy, TodoCategoryStrategy todoCategoryStrategy) {
        this.todayStrategy = todayStrategy;
        this.todoCategoryStrategy = todoCategoryStrategy;
    }

    @FXML
    private TextField todayContent;
    @FXML
    private TextField todayClocks;
    @FXML
    private ChoiceBox<String> categories;

    @FXML
    protected void onTodayAdd() {
        getContent().ifPresent(content ->
                getClocks().ifPresent(clocks ->
                        getSelectCategory().ifPresent(category -> {
                            var addDto = new TodayAddDto(content, clocks, category);
                            todayStrategy.add(addDto);

                            Stage stage = (Stage) todayContent.getScene().getWindow();
                            WindowUtil.close(stage);
                        })));
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onTodayAdd();
        }
    }

    private Optional<String> getSelectCategory() {
        return Optional.ofNullable(categories)
                .map(ChoiceBox::getValue)
                .filter(Predicate.not(String::isBlank));
    }

    private Optional<String> getContent() {
        return Optional.ofNullable(todayContent)
                .map(TextField::getText)
                .filter(Predicate.not(String::isBlank));
    }

    private Optional<Integer> getClocks() {
        return Optional.ofNullable(todayClocks)
                .map(TextField::getText)
                .filter(Predicate.not(String::isBlank))
                .map(Integer::parseInt);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todoCategoryStrategy.all()
                .ifPresent(categories -> this.categories
                        .setItems(FXCollections.observableList(
                                categories.stream().map(TodoCategoryDto::name).toList())));
    }
}

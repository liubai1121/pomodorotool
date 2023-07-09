package com.tool.pomodoro.technique.tool.controller.controller.today;

import com.tool.pomodoro.technique.tool.controller.controller.today.vo.TodayVo;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.strategy.service.today.TodayStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.today.dto.TodayUpdateDto;
import com.tool.pomodoro.technique.tool.strategy.service.todo.TodoCategoryStrategy;
import com.tool.pomodoro.technique.tool.strategy.service.todo.dto.TodoCategoryDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
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

public class TodayEditController implements Initializable {

    private final TodayStrategy todayStrategy;
    private final TodayVo today;
    private final TodoCategoryStrategy todoCategoryStrategy;

    public TodayEditController(TodayStrategy todayStrategy, TodayVo today, TodoCategoryStrategy todoCategoryStrategy) {
        this.todayStrategy = todayStrategy;
        this.today = today;
        this.todoCategoryStrategy = todoCategoryStrategy;
    }

    @FXML
    private Label todayId;
    @FXML
    private TextField todayContent;
    @FXML
    private TextField todayClocks;
    @FXML
    private Label todayCreateTime;
    @FXML
    private ChoiceBox<String> categories;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        todayId.setText(today.id());
        todayContent.setText(today.content());
        todayClocks.setText(String.valueOf(today.clocks()));
        todayCreateTime.setText(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(today.createTime()));

        todoCategoryStrategy.all()
                .ifPresent(categories -> this.categories
                        .setItems(FXCollections.observableList(
                                categories.stream().map(TodoCategoryDto::name).toList())));


        categories.setValue(today.category());
    }

    @FXML
    protected void onTodayEdit() {
        getId().ifPresent(id ->
                getContent().ifPresent(content ->
                        getClocks().ifPresent(clocks -> getSelectCategory().ifPresent(category -> {
                            var updateDto = new TodayUpdateDto(id, content, clocks, category);
                            todayStrategy.update(updateDto);

                            Stage stage = (Stage) todayId.getScene().getWindow();
                            WindowUtil.close(stage);
                        }))));
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onTodayEdit();
        }
    }

    private Optional<String> getId() {
        return Optional.ofNullable(todayId)
                .map(Label::getText)
                .filter(Predicate.not(String::isBlank));
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


}

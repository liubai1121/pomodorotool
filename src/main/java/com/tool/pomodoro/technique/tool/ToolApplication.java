package com.tool.pomodoro.technique.tool;

import com.tool.pomodoro.technique.tool.controller.ToolController;
import com.tool.pomodoro.technique.tool.common.queue.PerSecondCommandQueue;
import com.tool.pomodoro.technique.tool.strategy.service.today.factory.TodayStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.todo.factory.TodoStrategyFactory;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.impl.TodoTodayMoveStrategyImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ToolApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        PerSecondCommandQueue.init();


        var toolController = createController();

        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource("tool-view.fxml"));
        fxmlLoader.setController(toolController);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Pomodoro Technique Tool");
        stage.setScene(scene);
        stage.show();


    }


    private ToolController createController() {
        var todayStrategy = TodayStrategyFactory.create();
        var todoStrategy = TodoStrategyFactory.create();
        var moveStrategy = new TodoTodayMoveStrategyImpl(todoStrategy, todayStrategy);

        return new ToolController(todoStrategy, todayStrategy, moveStrategy);
    }

    public static void main(String[] args) {
        launch();
    }
}
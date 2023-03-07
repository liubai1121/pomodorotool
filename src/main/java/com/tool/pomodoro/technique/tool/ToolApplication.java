package com.tool.pomodoro.technique.tool;

import com.tool.pomodoro.technique.tool.controller.ToolController;
import com.tool.pomodoro.technique.tool.database.file.today.FileTodayDatabase;
import com.tool.pomodoro.technique.tool.database.file.todo.FileTodoDatabase;
import com.tool.pomodoro.technique.tool.init.ToolInit;
import com.tool.pomodoro.technique.tool.init.queue.CommandQueue;
import com.tool.pomodoro.technique.tool.init.queue.CommandQueueImpl;
import com.tool.pomodoro.technique.tool.strategy.service.today.iml.TodayStrategyImpl;
import com.tool.pomodoro.technique.tool.strategy.service.todo.impl.TodoStrategyImpl;
import com.tool.pomodoro.technique.tool.strategy.service.todotodaymove.impl.TodoTodayMoveStrategyImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ToolApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        var toolController = createController();

        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource("tool-view.fxml"));
        fxmlLoader.setController(toolController);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Pomodoro Technique Tool");
        stage.setScene(scene);
        stage.show();
    }


    private ToolController createController() {
        var TodoDatabase = new FileTodoDatabase();
        var todayDatabase = new FileTodayDatabase();
        var queue = new CommandQueueImpl();

        initTool(List.of(TodoDatabase, todayDatabase));


        var todoStrategy = new TodoStrategyImpl(TodoDatabase);
        var todayStrategy = new TodayStrategyImpl(todayDatabase);
        var moveStrategy = new TodoTodayMoveStrategyImpl(todoStrategy, todayStrategy);

        return new ToolController(todoStrategy,todayStrategy, moveStrategy, queue);
    }


    private void initTool(List<ToolInit> toolInit) {
        toolInit.forEach(ToolInit::init);
    }

    public static void main(String[] args) {
        launch();
    }

}
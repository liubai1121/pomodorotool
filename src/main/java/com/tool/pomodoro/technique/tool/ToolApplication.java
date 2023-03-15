package com.tool.pomodoro.technique.tool;

import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import com.tool.pomodoro.technique.tool.controller.controller.today.TodayController;
import com.tool.pomodoro.technique.tool.controller.controller.todo.TodoController;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolController;
import com.tool.pomodoro.technique.tool.controller.controller.tool.ToolMenuController;
import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.FileStorageStrategyFactory;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ToolApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StrategyFactory strategyFactory = new FileStorageStrategyFactory(() -> "D:\\Program Files\\Pomodoro Technique Tool\\");
        var toolController = new ToolController(strategyFactory);
        var menuController = new ToolMenuController(strategyFactory);
        var todoController = new TodoController(strategyFactory);
        var todayController = new TodayController(strategyFactory);

        Callback<Class<?>, Object> controllerFactory = type -> {
            if (type == ToolController.class) {
                return toolController;
            } else if (type == ToolMenuController.class) {
                return menuController;
            } else if (type == TodoController.class) {
                return todoController;
            } else if (type == TodayController.class) {
                return todayController;
            } else {
                try {
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc);
                }
            }
        };

        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource("tool/tool-view.fxml"));

        fxmlLoader.setControllerFactory(controllerFactory);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("番茄小工具");
        stage.setScene(scene);

        stage.setOnCloseRequest(event ->

        {
            System.exit(0);
        });

        stage.show();
    }

    private static void initTool() {
        PerSecondCommandScheduleQueue.getInstance().init();
    }

    public static void main(String[] args) {
        initTool();
        launch();
    }
}
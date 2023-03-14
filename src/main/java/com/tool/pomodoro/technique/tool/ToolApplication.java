package com.tool.pomodoro.technique.tool;

import com.tool.pomodoro.technique.tool.common.command.queue.PerSecondCommandScheduleQueue;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ToolApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initTool();

        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource("tool/tool-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("番茄工作法工具");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        stage.show();
    }

    private void initTool() {
        PerSecondCommandScheduleQueue.getInstance().init();
    }

    public static void main(String[] args) {
        launch();
    }
}
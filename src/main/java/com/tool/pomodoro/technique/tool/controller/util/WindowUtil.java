package com.tool.pomodoro.technique.tool.controller.util;

import com.tool.pomodoro.technique.tool.ToolApplication;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class WindowUtil {


    private WindowUtil() {
    }

    public static Stage create(String title, String fxmlPath) {
        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource(fxmlPath));

        return create(title, fxmlLoader);
    }

    public static Stage create(String title, String fxmlPath, Object controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(ToolApplication.class.getResource(fxmlPath));
        fxmlLoader.setController(controller);

        return create(title, fxmlLoader);
    }

    private static Stage create(String title, FXMLLoader fxmlLoader) {
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);

        return stage;
    }


    public static void close(Stage stage) {
        Optional<Stage> stageOpt = Optional.ofNullable(stage);

        stageOpt.map(Window::getOnCloseRequest)
                .ifPresent(onCloseEventHandler -> createCloseWindowEvent(stage).ifPresent(onCloseEventHandler::handle));

        stageOpt.ifPresent(Stage::close);
    }

    private static Optional<WindowEvent> createCloseWindowEvent(Stage stage) {
        return Optional.ofNullable(stage)
                .map(window -> new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}

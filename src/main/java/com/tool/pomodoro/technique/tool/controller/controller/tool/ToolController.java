package com.tool.pomodoro.technique.tool.controller.controller.tool;

import com.tool.pomodoro.technique.tool.controller.util.WindowUtil;
import com.tool.pomodoro.technique.tool.factory.StrategyFactory;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ToolController implements Initializable {

    private final StrategyFactory strategyFactory;

    public ToolController(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    private static final Map<String, Object> controllers = new HashMap<>();

    public static void registerController(Object controller) {
        controllers.put(controller.getClass().getName(), controller);
    }

    public static <T> Optional<T> getController(Class<T> controllerClass) {
        return Optional.ofNullable(controllerClass)
                .flatMap(clazz -> Optional.ofNullable(controllers.get(clazz.getName())).map(clazz::cast));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var menuController = new ToolMenuController(strategyFactory);
        var stage = WindowUtil.create("a", "tool/tool-menu.fxml", menuController);
        stage.show();
    }
}
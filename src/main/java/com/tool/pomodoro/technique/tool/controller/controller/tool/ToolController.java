package com.tool.pomodoro.technique.tool.controller.controller.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ToolController {

    private static final Map<String, Object> controllers = new HashMap<>();

    public static void registerController(Object controller) {
        controllers.put(controller.getClass().getName(), controller);
    }

    public static <T> Optional<T> getController(Class<T> controllerClass) {
        return Optional.ofNullable(controllerClass)
                .flatMap(clazz -> Optional.ofNullable(controllers.get(clazz.getName())).map(clazz::cast));
    }

}
package com.tool.pomodoro.technique.tool.strategy.util;

import java.util.UUID;

public class IdUtil {
    private IdUtil() {

    }

    public static String generate() {
        return UUID.randomUUID().toString();
    }

}

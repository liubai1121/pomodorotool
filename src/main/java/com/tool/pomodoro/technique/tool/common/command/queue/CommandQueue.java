package com.tool.pomodoro.technique.tool.common.command.queue;

import com.tool.pomodoro.technique.tool.common.command.Command;

public interface CommandQueue {
    void put(Command command);
}

package com.tool.pomodoro.technique.tool.common.queue;

import com.tool.pomodoro.technique.tool.common.queue.command.Command;

public interface CommandQueue {
    void put(Command command);
}

package com.tool.pomodoro.technique.tool.common.command.queue;

import com.tool.pomodoro.technique.tool.common.command.Command;

public interface CommandScheduleQueue {
    void put(Command command);

    void delete(Command command);
}

package com.tool.pomodoro.technique.tool.common.command.queue;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.init.ToolInit;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class PerSecondCommandScheduleQueue implements CommandScheduleQueue, ToolInit {
    private final static PerSecondCommandScheduleQueue PER_SECOND_COMMAND_QUEUE = new PerSecondCommandScheduleQueue();

    private final Queue<Command> queue = new LinkedList<>();

    private PerSecondCommandScheduleQueue() {
    }

    @Override
    public void init() {
        PerSecondCommandScheduleQueue.getInstance().put(new SleepCommand(1000));

        new Thread(() -> {
            while (true) {
                Optional.ofNullable(queue.poll())
                        .ifPresent(Command::execute);
            }
        }).start();
    }

    @Override
    public void put(Command command) {
        Optional.ofNullable(command)
                .ifPresent(queue::add);
    }

    public static PerSecondCommandScheduleQueue getInstance() {
        return PER_SECOND_COMMAND_QUEUE;
    }

    private static class SleepCommand implements Command {
        private final long sleepTime;

        public SleepCommand(long sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public void execute() {
            try {
                Thread.sleep(sleepTime);
                PerSecondCommandScheduleQueue.getInstance().put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

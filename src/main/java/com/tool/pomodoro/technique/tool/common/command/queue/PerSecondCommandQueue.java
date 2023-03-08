package com.tool.pomodoro.technique.tool.common.command.queue;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.init.ToolInit;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class PerSecondCommandQueue implements CommandQueue, ToolInit {
    private final static PerSecondCommandQueue PER_SECOND_COMMAND_QUEUE = new PerSecondCommandQueue();

    private final Queue<Command> queue = new LinkedList<>();

    private PerSecondCommandQueue() {
    }

    @Override
    public void init() {
        PerSecondCommandQueue.getInstance().put(new SleepCommand(1000));

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

    public static PerSecondCommandQueue getInstance() {
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
                PerSecondCommandQueue.getInstance().put(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

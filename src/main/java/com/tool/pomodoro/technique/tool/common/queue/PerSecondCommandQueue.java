package com.tool.pomodoro.technique.tool.common.queue;

import com.tool.pomodoro.technique.tool.common.command.Command;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class PerSecondCommandQueue {
    private final static Queue<Command> queue = new LinkedList<>();

    public static void init() {
        PerSecondCommandQueue.join(new SleepCommand(1000));

        new Thread(() -> {
            while (true) {
                Optional.ofNullable(queue.poll())
                        .ifPresent(Command::execute);
            }
        }).start();
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
                PerSecondCommandQueue.join(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void join(Command command) {
        queue.add(command);
    }
}

package com.tool.pomodoro.technique.tool.common.command.queue;

import com.tool.pomodoro.technique.tool.common.command.Command;
import com.tool.pomodoro.technique.tool.common.init.ToolInit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;
import java.util.Objects;
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

    @Override
    public void delete(Command command) {
        Optional.ofNullable(command)
                .ifPresent(queue::remove);
    }

    public static PerSecondCommandScheduleQueue getInstance() {
        return PER_SECOND_COMMAND_QUEUE;
    }

    private static class SleepCommand implements Command {
        private final long sleepTime;
        private LocalDateTime lastWokeUp;

        public SleepCommand(long sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public void execute() {
            long thisSleepTime = calculateThisSleepTime();
            if (thisSleepTime > 0) {
                sleep(thisSleepTime);
            }
            PerSecondCommandScheduleQueue.getInstance().put(this);
        }

        private long calculateThisSleepTime() {
            long thisSleepTime;
            if (Objects.isNull(lastWokeUp)) {
                thisSleepTime = sleepTime;
            } else {
                int lastWokeUpMilli = lastWokeUp.get(ChronoField.MILLI_OF_DAY);
                int nowMilli = LocalDateTime.now().get(ChronoField.MILLI_OF_DAY);
                thisSleepTime = sleepTime - (nowMilli - lastWokeUpMilli);
            }
            return thisSleepTime;
        }

        private void sleep(long sleepTime) {
            try {
                Thread.sleep(sleepTime);
                lastWokeUp = LocalDateTime.now();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

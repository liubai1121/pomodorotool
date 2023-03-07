package com.tool.pomodoro.technique.tool.init.queue;

import com.tool.pomodoro.technique.tool.init.ToolInit;
import javafx.application.Platform;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class CommandQueueImpl implements CommandQueue, ToolInit {
    private final static Queue<Command> queue = new LinkedList<>();

    @Override
    public void init() {
        DelayCommand delayCommand = new DelayCommand(this);
        queue.add(delayCommand);

        Platform.runLater(() -> {
            while (true) {
                Optional.ofNullable(queue.poll())
                        .ifPresent(Command::execute);
                System.out.println("test");
            }
        });
    }

    @Override
    public void join(Command command) {
        queue.add(command);
    }
}

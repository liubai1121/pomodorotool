package com.tool.pomodoro.technique.tool.init.queue;

public class DelayCommand implements Command {

    private CommandQueue commandQueue;

    public DelayCommand (CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }
    @Override
    public void execute() {
        try {
            Thread.sleep(1000);
            commandQueue.join(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

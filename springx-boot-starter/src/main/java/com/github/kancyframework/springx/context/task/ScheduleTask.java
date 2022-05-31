package com.github.kancyframework.springx.context.task;

import com.github.kancyframework.springx.utils.DurationStyle;

/**
 * ScheduleTask
 *
 * @author huangchengkang
 * @date 2022/5/31 18:37
 */
public class ScheduleTask implements Runnable{
    private final Long period;
    private final Runnable task;
    private Long round;

    public ScheduleTask(Long period, Runnable task) {
        this.period = period;
        this.task = task;
        this.resetRound();
    }

    public ScheduleTask(String period, Runnable task) {
        this(DurationStyle.detectAndParse(period).toMillis(), task);
    }

    private boolean canRun(){
        return this.round <= 1;
    }


    private void resetRound() {
        this.round = period <= ScheduleTaskManager.ONE_PERIOD
                ? 1 : period / ScheduleTaskManager.ONE_PERIOD;
    }

    void handleTask(Runnable runnable){
        if (canRun()){
            resetRound();
            runnable.run();
        } else {
            this.round--;
        }
    }

    @Override
    public void run() {
        this.task.run();
    }

    public String getTaskName(){
        return task.getClass().getName();
    }
}

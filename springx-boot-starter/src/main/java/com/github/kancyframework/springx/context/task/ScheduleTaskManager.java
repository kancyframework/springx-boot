package com.github.kancyframework.springx.context.task;

import com.github.kancyframework.springx.context.InitializingBean;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ScheduleTaskManager
 *
 * @author huangchengkang
 * @date 2022/5/31 18:37
 */
public class ScheduleTaskManager implements InitializingBean {
    public static final Long ONE_PERIOD = 100L;

    private final List<ScheduleTask> scheduleTasks = new LinkedList<>();

    private final ScheduledThreadPoolExecutor dispatcher;
    private final ExecutorService worker;

    public ScheduleTaskManager() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ScheduleTaskManager(int maxWorkerThreads) {
        this.dispatcher = new ScheduledThreadPoolExecutor(1);
        this.worker = new ThreadPoolExecutor(Math.max(maxWorkerThreads/2, 1), maxWorkerThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory("scheduleTaskWorker-"));
    }

    private void scheduleTasks() {
        dispatcher.scheduleAtFixedRate(() -> {
            LinkedList<ScheduleTask> currentScheduleTasks = new LinkedList<>(ScheduleTaskManager.this.scheduleTasks);
            for (ScheduleTask scheduleTask : currentScheduleTasks) {
                scheduleTask.handleTask(() -> worker.submit(scheduleTask));
            }
        }, ONE_PERIOD, ONE_PERIOD, TimeUnit.MILLISECONDS);
    }

    public void submitTask(ScheduleTask task){
        synchronized (scheduleTasks){
            scheduleTasks.add(task);
        }
    }

    public void submitTask(Long period, Runnable task){
        submitTask(new ScheduleTask(period, task));
    }

    public void submitTask(String period, Runnable task){
        submitTask(new ScheduleTask(period, task));
    }

    public void shutdown(){
        dispatcher.shutdown();
        worker.shutdown();
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied, {@code ApplicationContextAware} etc.
     */
    @Override
    public void afterPropertiesSet() {
        // 注入ScheduleTask
        scheduleTasks();
    }
}

import java.util.concurrent.*;

public class main{
    public static void main(String[] args) {
        BlockingQueue<LogEvent> queue = new ArrayBlockingQueue<>(50);
        LogGenerator generator = new LogGenerator(queue);
        Thread generatorThread = new Thread(generator);
        generatorThread.start();
        
        //Processing Pipeline Stages
        Aggregator aggregator = new Aggregator();
        ExecutorService processorPool = Executors.newFixedThreadPool(4);
        Thread dispatcherThread = new Thread(() ->{
            while(!Thread.currentThread().isInterrupted()){
                try {
                    LogEvent event = queue.take();
                    processorPool.submit(new LogProcessor(event, aggregator));
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        dispatcherThread.start();

        ScheduledExecutorService statsPrinter = Executors.newSingleThreadScheduledExecutor();
        statsPrinter.scheduleAtFixedRate(aggregator::printStats, 2, 3, TimeUnit.SECONDS);

        //Terminate Gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down log generator...");
            generatorThread.interrupt();
            dispatcherThread.interrupt();
            processorPool.shutdown();
            statsPrinter.shutdown();
        }));
    }
}
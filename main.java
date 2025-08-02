import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<LogEvent> queue = new ArrayBlockingQueue<>(50);
        try{
            FileWriter writer = new FileWriter("output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogGenerator generator = new LogGenerator(queue);
        Thread generatorThread = new Thread(generator);
        generatorThread.start();

        AtomicStatusUpdater atomicStatusUpdater = new AtomicStatusUpdater();
        VirtualThreadProcessor virtualThreadProcessor = new VirtualThreadProcessor(queue, atomicStatusUpdater);

        // 🧵 Use virtual threads for processing log events
        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        for (int i = 0; i < 10; i++) {
            virtualExecutor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        LogEvent event = queue.take(); // block until an event is available
                        virtualThreadProcessor.processEvent(event);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        // 🕒 Schedule periodic stats printer
        ScheduledExecutorService statsPrinter = Executors.newSingleThreadScheduledExecutor();
        statsPrinter.scheduleAtFixedRate(
            atomicStatusUpdater::printStats, 5, 5, TimeUnit.SECONDS
        );

        // Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown signal received.");
            generatorThread.interrupt();
            statsPrinter.shutdown();

            try {
                generatorThread.join();
                virtualExecutor.shutdownNow();
                virtualExecutor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            atomicStatusUpdater.printStats(); // Final stats
        }));

        // Let it run for a while then exit (or press Ctrl+C)
        Thread.sleep(30000); // Run for 30 seconds
        System.exit(0);
    }
}

public class LogProcessor implements Runnable {
    private final LogEvent event;
    private final AtomicStatusUpdater atomicStatusUpdater;

    public LogProcessor(LogEvent event, AtomicStatusUpdater atomicStatusUpdater) {
        this.event = event;
        this.atomicStatusUpdater = atomicStatusUpdater;
    }

    @Override
    public void run() {
        // Only process successful (status 200) events
        if (event.getStatus() != 200) {
            return;
        }

        try {
            Thread.sleep(10);  // Simulate processing time
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt(); // Best practice: reset interrupt flag
        }

        // Send processed event for atomic aggregation
        atomicStatusUpdater.update(event);
    }
}

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class VirtualThreadProcessor {
    private final BlockingQueue<LogEvent> queue;
    private final AtomicStatusUpdater statusUpdater;
    private final BufferedWriter writer;

    public VirtualThreadProcessor(BlockingQueue<LogEvent> queue, AtomicStatusUpdater statusUpdater) {
        this.queue = queue;
        this.statusUpdater = statusUpdater;

        try {
            this.writer = new BufferedWriter(new FileWriter("output.txt", true)); // append mode
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file writer", e);
        }
    }

    public void processEvent(LogEvent event) {
        try {
            String line = String.format("%s | %s | %s\n",
                    event.getTimestamp(),
                    event.getLevel(),
                    event.getMessage());
            synchronized (writer) {
                writer.write(line);
                writer.flush();
            }

            statusUpdater.increment(event.getLevel());

        } catch (IOException e) {
            System.err.println("Error writing log event: " + e.getMessage());
        }
    }
}

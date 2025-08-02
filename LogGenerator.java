import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class LogGenerator implements Runnable {
    private final BlockingQueue<LogEvent> queue;
    private final Random random = new Random();
    private final String[] endpoints = {"/api/user", "/api/order", "/api/login", "/api/item"};

    public LogGenerator(BlockingQueue<LogEvent> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                LogEvent log = generateFakeLogEvent();
                queue.put(log);  // Simulate producer placing logs into queue
                Thread.sleep(50); // Simulate time delay between log generations
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Respect thread interruption
            }
        }
    }

    private LogEvent generateFakeLogEvent() {
        long timestamp = System.currentTimeMillis();
        String ip = "192.168.1." + random.nextInt(255);
        String endpoint = endpoints[random.nextInt(endpoints.length)];
        int status = random.nextInt(10) < 8 ? 200 : 500;  // 80% success rate
        int latency = 50 + random.nextInt(400);           // Latency between 50–450ms
        return new LogEvent(timestamp, ip, endpoint, status, latency);
    }
}

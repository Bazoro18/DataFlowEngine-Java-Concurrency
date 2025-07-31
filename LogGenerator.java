import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class LogGenerator implements Runnable {
    private final BlockingQueue<LogEvent> queue;
    private final Random rand = new Random();
    private final String[] endpoints = {"/api/user", "/api/order", "/api/login", "/api/item"};

    public LogGenerator(BlockingQueue<LogEvent> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                LogEvent log = generateFakLogEvent();
                queue.put(log);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private LogEvent generateFakLogEvent() {
        long timestamp = System.currentTimeMillis();
        String ip = "192.168.1." + rand.nextInt(255);
        String endpoint = endpoints[rand.nextInt(endpoints.length)];
        int status = rand.nextInt(10) < 8 ? 200 : 500;
        int latency = 50 + rand.nextInt(400);
        return new LogEvent(timestamp, ip, endpoint, status, latency);
    }
}

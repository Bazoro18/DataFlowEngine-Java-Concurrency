import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class AtomicStatusUpdater {

    // Holds latency stats per endpoint
    public static class EndpointStats {
        public final LongAdder requestCount = new LongAdder();
        public final LongAdder totalLatency = new LongAdder();
        public final AtomicLong minLatency = new AtomicLong(Long.MAX_VALUE);
        public final AtomicLong maxLatency = new AtomicLong(Long.MIN_VALUE);

        public void record(int latency) {
            requestCount.increment();
            totalLatency.add(latency);
            updateMin(latency);
            updateMax(latency);
        }

        private void updateMin(int latency) {
            long prev;
            do {
                prev = minLatency.get();
                if (latency >= prev) return;
            } while (!minLatency.compareAndSet(prev, latency));
        }

        private void updateMax(int latency) {
            long prev;
            do {
                prev = maxLatency.get();
                if (latency <= prev) return;
            } while (!maxLatency.compareAndSet(prev, latency));
        }

        public double getAverageLatency() {
            long count = requestCount.sum();
            return count == 0 ? 0 : (double) totalLatency.sum() / count;
        }
    }

    // Thread-safe map storing stats by endpoint
    private final ConcurrentHashMap<String, EndpointStats> statsMap = new ConcurrentHashMap<>();

    public void update(LogEvent event) {
        statsMap
            .computeIfAbsent(event.getEndpoint(), key -> new EndpointStats())
            .record(event.getLatency());
    }

    public ConcurrentHashMap<String, EndpointStats> getStatsMap() {
        return statsMap;
    }

    public void printStats() {
        System.out.println("\n===== Endpoint Statistics =====");
        statsMap.forEach((endpoint, stats) -> {
            long count = stats.requestCount.sum();
            long totalLatency = stats.totalLatency.sum();
            long min = stats.minLatency.get();
            long max = stats.maxLatency.get();
            double avg = stats.getAverageLatency();

            System.out.printf(
                "Endpoint: %-20s | Count: %-6d | Avg Latency: %6.2f ms | Min: %-4d | Max: %-4d\n",
                endpoint, count, avg, min, max
            );
        });
    }
}

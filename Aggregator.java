/*import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aggregator{
    private final Map<String, Stats> latencymap = new ConcurrentHashMap<>();

    public void update(LogEvent event){
        String endpoint = event.endpoint;
        String threadName = Thread.currentThread().getName();
        System.out.printf("[%s] Creating new Stats for %s\n", threadName, endpoint);
        Stats stats = latencymap.computeIfAbsent(endpoint, key -> new Stats());
        try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        System.out.printf("[%s] Processing %s -> %dms\n", threadName, endpoint, event.latency);
        synchronized (stats) {
            stats.totalLatency += event.latency;
            stats.count++;  
        }
    }

    public void printStats(){
        System.out.println("==Aggregated Latency Stats==");
        for(Map.Entry<String, Stats> entry : latencymap.entrySet()){
            Stats stats = entry.getValue();
            synchronized (stats) {
                System.out.printf("%s -> avg :%.2fms, count: %d%n", 
                              entry.getKey(), 
                              stats.getAvg(),
                              stats.count);
            }
        }
    }
}
class Stats {
    long totalLatency = 0;
    int count = 0;

    public double getAvg() {
        return count == 0 ? 0 : (double) totalLatency / count;
    }
}*/
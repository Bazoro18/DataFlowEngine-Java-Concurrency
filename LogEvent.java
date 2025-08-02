import java.time.Instant;

public class LogEvent {
    private final long timestamp;
    private final String ip;
    private final String endpoint;
    private final int status;
    private final int latency;

    public LogEvent(long timestamp, String ip, String endpoint, int status, int latency) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.endpoint = endpoint;
        this.status = status;
        this.latency = latency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getIp() {
        return ip;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getStatus() {
        return status;
    }

    public int getLatency() {
        return latency;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s %d %dms",
                Instant.ofEpochMilli(timestamp), ip, endpoint, status, latency);
    }
}

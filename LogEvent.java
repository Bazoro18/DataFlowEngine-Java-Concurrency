//package model;
import java.time.Instant;

public class LogEvent {
    public long timestamp;
    public String ip;
    public String endpoint;
    public int status;
    public int latency;

    public LogEvent(long timestamp, String ip, String endpoint, int status, int latency) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.endpoint = endpoint;
        this.status = status;
        this.latency = latency;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s %d %dms", Instant.ofEpochMilli(timestamp), ip, endpoint, status, latency);
    }
}
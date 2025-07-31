public class LogProcessor implements Runnable{
    private final LogEvent event;
    private final Aggregator aggregator;

    public LogProcessor(LogEvent event, Aggregator aggregator) {
        this.event = event;
        this.aggregator = aggregator;
    }

    @Override
    public void run(){
        if(event.status != 200){
            return;
        }
        //int normalizedLatency = Math.min(300,event.latency);
        //System.out.printf("Processed [%s] %s -> %dms%n", event.endpoint, event.ip, normalizedLatency);
        try {
            Thread.sleep(100);
        } catch (InterruptedException  ignored) {
        }
        aggregator.update(event);
    }
}
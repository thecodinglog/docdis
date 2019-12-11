package cothe.docdis;

public class LoggerFactory {
    public static Logger getLogger() {
        return new SystemOutLogger();
    }
}

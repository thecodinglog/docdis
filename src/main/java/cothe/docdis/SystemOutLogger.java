package cothe.docdis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemOutLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message);
    }
}

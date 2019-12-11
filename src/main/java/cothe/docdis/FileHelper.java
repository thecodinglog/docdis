package cothe.docdis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class FileHelper {
    public static String makeCurrentDayDirectory(String baseDir) throws IOException {
        Path directories = Files.createDirectories(Paths.get(baseDir + (baseDir.endsWith("/") ? "" : "/") + getCurrentDayDirectory()));
        return directories.toString();
    }

    public static String getCurrentDayDirectory() {
        String year = String.format("%04d", LocalDateTime.now().getYear());
        String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        String day = String.format("%02d", LocalDateTime.now().getDayOfMonth());
        return year + month + day;
    }
}

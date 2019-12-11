package cothe.docdis;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Settings {
    private String sourceDir;
    private List<String> targetDirs;
    private String backupDir;
    private int cycle;
    private String errorDir;

    public Settings(String sourceDir, List<String> targetDirs, String backupDir, int cycle, String errorDir) {
        this.sourceDir = sourceDir;
        this.targetDirs = targetDirs;
        this.backupDir = backupDir;
        this.cycle = cycle;
        this.errorDir = errorDir;
    }

    public static Settings loadSettingFile(String fileName) {
        Gson gson = new Gson();
        Settings settings;
        try (FileReader fileReader = new FileReader(fileName)) {
            settings = gson.fromJson(fileReader, Settings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        omitEndingSlashOfSettings(settings);
        return settings;
    }

    private static void omitEndingSlashOfSettings(Settings settings) {
        settings.sourceDir = omitEndingSlash(settings.sourceDir);
        for (int i = 0; i < settings.targetDirs.size(); i++) {
            settings.targetDirs.set(i, omitEndingSlash(settings.targetDirs.get(i)));
        }
        settings.backupDir = omitEndingSlash(settings.backupDir);
        settings.errorDir = omitEndingSlash(settings.errorDir);
    }

    public static String omitEndingSlash(String str) {
        if (str.endsWith("/")) {
            return str.substring(0, str.length() - 1);
        } else {
            return str;
        }
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public List<String> getTargetDirs() {
        return targetDirs;
    }

    public String getBackupDir() {
        return backupDir;
    }

    public int getCycle() {
        return cycle;
    }

    public String getErrorDir() {
        return errorDir;
    }
}
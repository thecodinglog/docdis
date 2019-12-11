package cothe.docdis;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;

public class SettingsTest {
    String settingFilePath = "c:/intergis/settings.json";

    @Test
    public void loadSettings() {
        Settings settings = Settings.loadSettingFile(settingFilePath);
        assertNotNull(settings);
    }

    @Test
    public void loadSettingsByPath() throws Exception {
        Settings settings = Settings.loadSettingFile(settingFilePath);

    }

    @Test
    public void getSourceFileList() {
        Settings settings = Settings.loadSettingFile(settingFilePath);
        assertNotNull(settings);
        File sourceDirectory = new File(settings.getSourceDir());
        assertNotNull(sourceDirectory);
    }

    @Test
    public void getPathFromString() {
        Settings settings = Settings.loadSettingFile(settingFilePath);
        assertNotNull(settings);
        Path path = Paths.get(settings.getTargetDirs().get(0));
        System.out.println(path.toAbsolutePath());
    }

    @Test
    public void makeTargetFileName() {
        Settings settings = Settings.loadSettingFile(settingFilePath);
        assertNotNull(settings);
        File sourceDirectory = new File(settings.getSourceDir());
        for (File file : sourceDirectory.listFiles()) {
            System.out.println(file.getName());
        }
    }

}
package cothe.docdis;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FileDistributorTest {
    @Test
    public void run() throws Exception {
        Settings settings = Settings.loadSettingFile("c:/intergis/settings.json");
        assertNotNull(settings);
        FileDistributor fileDistributor = new FileDistributor(settings);
        fileDistributor.start();
    }
}
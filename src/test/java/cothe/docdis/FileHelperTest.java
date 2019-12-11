package cothe.docdis;

import org.junit.Test;

import java.io.IOException;

public class FileHelperTest {
    @Test
    public void makeDayDirTest() throws IOException {
        String s = FileHelper.makeCurrentDayDirectory("c:/intergis/error");
        System.out.println(s);
    }

    @Test
    public void makeDayDirTestEndingSlash() throws IOException {
        FileHelper.makeCurrentDayDirectory("c:/intergis/error/");
    }
}
package cothe.docdis;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileDistributor {
    private Settings settings;
    private Logger logger;

    public FileDistributor(Settings settings, Logger logger) {
        this.settings = settings;
        this.logger = logger;
    }

    public void start() throws Exception {
        while (true) {
            logger.log("Scan the source directory.");
            File sourceDirectory = new File(settings.getSourceDir());
            File[] files = sourceDirectory.listFiles();
            if (files == null) {
                Thread.sleep(settings.getCycle() * 1000);
                continue;
            } else if (files.length == 0) {
                Thread.sleep(settings.getCycle() * 1000);
                continue;
            }

            for (File sourceFile : files) {
                for (String targetDir : settings.getTargetDirs()) {
                    try {
                        Files.copy(sourceFile.toPath(), Paths.get(targetDir + "/" + sourceFile.getName()));
                    } catch (FileAlreadyExistsException e) {
                        logger.log(sourceFile.getName() + " is already exists at " + targetDir);
//                        logger.log(sourceFile.getName() + " will be copied to error directory.");
//                        String dirForError = FileHelper.makeCurrentDayDirectory(settings.getErrorDir());
//                        Files.copy(sourceFile.toPath(), Paths.get(dirForError + "/"
//                                + UUID.randomUUID()
//                                + sourceFile.getName()));
                    }
                }
            }
//            logger.log(files.length + " files are copied to " + settings.getTargetDirs().size() + " target directories.");

            String dirForMove = FileHelper.makeCurrentDayDirectory(settings.getBackupDir());
            for (File sourceFile : files) {
                try {
                    Files.move(sourceFile.toPath(), Paths.get(dirForMove + "/" + sourceFile.getName()));
                } catch (FileAlreadyExistsException e) {
                    logger.log(sourceFile.getName() + " is already exists at backup directory.");
                    logger.log(sourceFile.getName() + " will be copied to error directory.");
                    String dirForError = FileHelper.makeCurrentDayDirectory(settings.getErrorDir());
                    Files.move(sourceFile.toPath(), Paths.get(dirForError + "/"
                            + UUID.randomUUID()
                            + sourceFile.getName()));
                }
            }
            logger.log(files.length + " files are copied and moved.");
            Thread.sleep(settings.getCycle() * 1000);
        }
    }
}

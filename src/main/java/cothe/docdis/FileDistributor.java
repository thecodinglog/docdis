package cothe.docdis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileDistributor {
    public static final String TEMP = "Temp";
    private static Logger logger = LogManager.getLogger(Controller.class);
    private final Settings settings;
    private final JandiConnectClient jandiConnectClient;

    public FileDistributor(Settings settings) {
        this.settings = settings;
        jandiConnectClient = new JandiConnectClient(settings.getJandiConnectUrl());
    }

    public void start() throws Exception {
        while (true) {
            logger.info("Scan the source directory.");
            File sourceDirectory = new File(settings.getSourceDir());
            File[] scanFiles = sourceDirectory.listFiles();
            if (scanFiles == null) {
                Thread.sleep(settings.getCycle() * 1000);
                continue;
            } else if (scanFiles.length == 0) {
                Thread.sleep(settings.getCycle() * 1000);
                continue;
            }

            Set<File> greaterThan0Files = Arrays.stream(scanFiles).filter(File::isFile)
                    .filter(file -> file.length() > 0).collect(Collectors.toSet());
            Set<File> exceptedFiles = new HashSet<>();

            if (greaterThan0Files.size() == 0) {
                Thread.sleep(settings.getCycle() * 1000);
                continue;
            }

            //for copy
            for (String targetDir : settings.getTargetDirs()) {
                Files.createDirectories(Paths.get(targetDir + TEMP));

                for (File sourceFile : greaterThan0Files) {
                    try {
                        Files.copy(sourceFile.toPath(), Paths.get(targetDir + TEMP + "/" + sourceFile.getName()));
                        logger.info(sourceFile.getName() + " is copied to " + targetDir + TEMP);
                    } catch (Exception e) {
                        exceptedFiles.add(sourceFile);
                        logger.error(sourceFile.getName() + " :copy to temp: " + e.getMessage(), e);
                        jandiConnectClient.sendMessage(sourceFile.getName() + " :: " + e.getMessage());
                    }
                }
            }

            //for move
            for (String targetDir : settings.getTargetDirs()) {
                File[] copiedFiles = (new File(targetDir + TEMP)).listFiles();
                if (copiedFiles == null)
                    continue;

                for (File tempFile : copiedFiles) {
                    try {
                        Files.move(tempFile.toPath(), Paths.get(targetDir + "/" + tempFile.getName()));
                        logger.info(tempFile.getName() + " is moved to " + targetDir + TEMP);
                    } catch (FileAlreadyExistsException e) {
                        logger.error(tempFile.getName() + " is already exists at " + targetDir, e);
                        jandiConnectClient.sendMessage(tempFile.getName() + " is already exists at " + targetDir);
                    } catch (Exception e) {
                        logger.error(tempFile.getName() + " :: " + e.getMessage(), e);
                        jandiConnectClient.sendMessage(tempFile.getName() + " :: " + e.getMessage());
                    }
                }
            }

            Thread.sleep(1000);

            String dirToMove = FileHelper.makeCurrentDayDirectory(settings.getBackupDir());
            for (File sourceFile : greaterThan0Files.stream()
                    .filter(file -> !exceptedFiles.contains(file)).collect(Collectors.toSet())) {
                try {
                    Files.move(sourceFile.toPath(), Paths.get(dirToMove + "/" + sourceFile.getName()));
                    logger.info(sourceFile.getName() + " is moved to " + dirToMove);
                } catch (FileAlreadyExistsException e) {
                    logger.error(sourceFile.getName() + " is already exists at backup directory.");
                    logger.error(sourceFile.getName() + " will be copied to error directory.");
                    String dirForError = FileHelper.makeCurrentDayDirectory(settings.getErrorDir());
                    Files.move(sourceFile.toPath(), Paths.get(dirForError + "/"
                            + UUID.randomUUID() + "_"
                            + sourceFile.getName()));
                    jandiConnectClient.sendMessage(sourceFile.getName() + " is already exists at backup directory.");
                } catch (Exception e) {
                    logger.error(sourceFile.getName() + " :: " + e.getMessage(), e);
                    String dirForError = FileHelper.makeCurrentDayDirectory(settings.getErrorDir());
                    Files.move(sourceFile.toPath(), Paths.get(dirForError + "/"
                            + UUID.randomUUID() + "_"
                            + sourceFile.getName()));
                    jandiConnectClient.sendMessage(sourceFile.getName() + " :: " + e.getMessage());
                }
            }
            logger.info(greaterThan0Files.size() + " files are copied and moved.");
            Thread.sleep(settings.getCycle() * 1000);
        }
    }
}

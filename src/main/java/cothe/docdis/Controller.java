package cothe.docdis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Controller {
    /**
     * 설정 파일을 가져온다.json
     * 설정 파일에 Source, targets, backup 들 Dir을 가져 온다.
     * 설정 파일에 pulling 주기를 가져온다.
     * 주 프로세스를 가져와서 시작한다.
     * 소스에 파일 리스트를 가져온다.
     * 대상 리스트를 순환하면서 리스트에 있는 파일을 복사한다.
     * 복사가 끝나면 backup에 대상 파일들을 move 한다.
     * 연->월->일 로 dir 구분하여 mov
     *
     * @param args 설정파일경로
     * @throws Exception 설정한 경로에 디렉터리가 없으면 예외가 발생한다.
     */
    public static void main(String[] args) throws Exception {
        Logger logger = LogManager.getLogger(Controller.class);

        if (args.length == 0) {
            throw new IllegalArgumentException("Setting file path is required");
        }

        logger.info("Start docdis");

        String settingFilePath = args[0];
        Settings settings = Settings.loadSettingFile(settingFilePath);

        JandiConnectClient jandiConnectClient = new JandiConnectClient(settings.getJandiConnectUrl());
        jandiConnectClient.sendMessage("Start Docdis");

        FileDistributor fileDistributor = new FileDistributor(settings);
        while (true) {
            try {
                fileDistributor.start();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                jandiConnectClient.sendMessage("Process terminated :: " + e.getMessage());
            }
            Thread.sleep(60000);
            jandiConnectClient.sendMessage("Restart scan.");
            logger.info("Restart scan.");
        }
    }
}

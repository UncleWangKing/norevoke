
import java.io.IOException;
import java.nio.file.*;

public class Main {
    private static final String SOURCE_FILE_PATH = "C:/Users/28529/Documents/Tencent Files/285296372/Image";
    private static final String TARGET_FILE_PATH = System.getProperty("user.dir") + "/backup";
    private static final long TIME_LIMIT = 130000L;

    public static void main(String[] args) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        ResourceListener resourceListener = new ResourceListener(watchService, SOURCE_FILE_PATH, new ResourceHandler(SOURCE_FILE_PATH, TARGET_FILE_PATH, TIME_LIMIT));

        Path p = Paths.get(SOURCE_FILE_PATH);
        p.register(resourceListener.getService(), StandardWatchEventKinds.ENTRY_MODIFY);

        resourceListener.run();
    }
}


import java.io.IOException;

public class Main {
    private static final String TARGET_FILE_PATH = "C:/Users/28529/Documents/Tencent Files/285296372/Image";

    public static void main(String[] args) throws IOException {
        ResourceHandler.init(TARGET_FILE_PATH);
        ResourceHandler.createDirIfNotExists();
        ResourceListener.addListener(TARGET_FILE_PATH);
    }
}

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ResourceHandler {
    private static final String TARGET_FILE_PATH = System.getProperty("user.dir") + "/backup";
    private static final long timeLimit = 13000L;
    private static String SOURCE_FILE_PATH = "";

    public static void init(String sourceFilePath){
        SOURCE_FILE_PATH = sourceFilePath;
    }

    public static void doUpdate() throws IOException {
        Map<String, File> sourceMap = getAllFileByPath(SOURCE_FILE_PATH);
        Map<String, File> targetMap = getAllFileByPath(TARGET_FILE_PATH);
        doCopy(sourceMap, targetMap);
        doCancel(targetMap);
    }

    private static void doCancel(Map<String, File> targetMap) throws IOException {
        for (Map.Entry<String, File> entry : targetMap.entrySet()){
            File file = entry.getValue();
            if(creatMoreThan(entry.getValue(), timeLimit)){
                file.delete();
            }
        }
    }

    private static boolean creatMoreThan(File file, long limit) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(),BasicFileAttributes.class);
        return System.currentTimeMillis() - basicFileAttributes.creationTime().toMillis() > limit;
    }

    private static void doCopy(Map<String, File> sourceMap, Map<String, File> targetMap) throws IOException {
        if(0 == sourceMap.size()) {
            return;
        }
        for (Map.Entry<String, File> entry:sourceMap.entrySet()) {
            if(! targetMap.containsKey(entry.getKey()) && ! creatMoreThan(entry.getValue(), timeLimit)){
                FileUtils.copyFile(entry.getValue(), new File(TARGET_FILE_PATH + "/" + entry.getKey()));
            }
        }
    }

    public static void createDirIfNotExists(){
        File dir = new File(TARGET_FILE_PATH);
        if (! dir.exists()) {
            dir.mkdirs();
        }
    }

    private static Map<String, File> getAllFileByPath(String path) {
        int fileNum = 0, folderNum = 0;
        Map<String, File> resultMap = new HashMap<>();
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    list.add(file2);
                } else {
                    resultMap.put(file2.getName(), file2);
                    fileNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        list.add(file2);
                        folderNum++;
                    } else {
                        resultMap.put(file2.getName(), file2);
                        fileNum++;
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);

        return resultMap;
    }
}

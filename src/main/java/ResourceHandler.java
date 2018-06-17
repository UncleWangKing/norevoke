import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ResourceHandler {
    private String sourceFilePath = "";
    private String targetFilePath = "";
    private long timeLimit = 0L;

    public ResourceHandler(String sourceFilePath, String targetFilePath, long timeLimit){
        this.targetFilePath = targetFilePath;
        this.sourceFilePath = sourceFilePath;
        this.timeLimit = timeLimit;
        createDirIfNotExists();
    }

    public void doUpdate() throws IOException {
        Map<String, File> sourceMap = getAllFileByPath(sourceFilePath);
        Map<String, File> targetMap = getAllFileByPath(targetFilePath);
        doCopy(sourceMap, targetMap);
        doCancel(sourceMap, targetMap);
    }

    private void doCancel(Map<String, File> sourceMap, Map<String, File> targetMap) throws IOException {
        for (Map.Entry<String, File> entry : targetMap.entrySet()){
            File file = entry.getValue();
            if(sourceMap.containsKey(entry.getKey()) && createMoreThan(entry.getValue())){
                file.delete();
            }
        }
    }

    private boolean createMoreThan(File file) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(),BasicFileAttributes.class);
        long timeNow = System.currentTimeMillis();
        long timeCreated = basicFileAttributes.creationTime().toMillis();

        return timeNow - timeCreated > timeLimit;
    }

    private void doCopy(Map<String, File> sourceMap, Map<String, File> targetMap) throws IOException {
        if(0 == sourceMap.size()) {
            return;
        }
        for (Map.Entry<String, File> entry:sourceMap.entrySet()) {
            if(! targetMap.containsKey(entry.getKey()) && ! createMoreThan(entry.getValue())){
                FileUtils.copyFile(entry.getValue(), new File(targetFilePath + "/" + entry.getKey()));
            }
        }
    }

    private void createDirIfNotExists(){
        File dir = new File(targetFilePath);
        if (! dir.exists()) {
            dir.mkdirs();
        }
    }

    private Map<String, File> getAllFileByPath(String path) {
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

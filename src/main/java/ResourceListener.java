import java.io.IOException;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

class ResourceListener implements Runnable {
    private WatchService service;
    private String rootPath;
    private ResourceHandler resourceHandler;

    public WatchService getService() {
        return service;
    }

    public ResourceListener(WatchService service, String rootPath, ResourceHandler resourceHandler) {
        this.service = service;
        this.rootPath = rootPath;
        this.resourceHandler = resourceHandler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WatchKey watchKey = service.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> event : watchEvents) {

                    //TODO 根据事件类型采取不同的操作。。。。。。。
                    System.out.println("[" + rootPath + "\\" + event.context() + "]文件发生了[" + event.kind() + "]事件" + event.count());
                    resourceHandler.doUpdate();
                }
                watchKey.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                service.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
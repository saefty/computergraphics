package taher858897.a05.Threading.Executors;

import taher858897.Image;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Threading.SampleMultithread;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class RayTraceExcecutor {
    private ExecutorService executorService;
    List<Callable<RayTraceTask>> rayTraceTasks;

    public static int started = 0;
    public static int counter = 0;

    public static long startTime;

    public static int threadIds=0;

    class NamedThreadFactory implements ThreadFactory{
        @Override
        public Thread newThread(Runnable r) {
            threadIds++;
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    t.interrupt();
                    e.printStackTrace();
                }
            });
            t.setName("T-" + threadIds);
            return t;
        }
    }

    public RayTraceExcecutor(int threadCount){
        executorService = Executors.newFixedThreadPool(threadCount, new NamedThreadFactory());
        rayTraceTasks = new ArrayList<>();
    }

    public Image executeTracer(Image img, RayTracer r, int sampleRate, int taskCount, int imgCount){
        startTime = System.currentTimeMillis();
        started = taskCount;
        int rate = sampleRate/taskCount;
        int lastRate = rate;
        if (rate*taskCount != sampleRate){
            lastRate = (sampleRate - rate);
        }
        ArrayList<Image> images = new ArrayList<>();
        for (int i = 0; i < imgCount; i++) {
            images.add(new Image(img));
        }
        for (int i = 0; i < taskCount; i++) {
            int curRate = i == taskCount - 1 ? lastRate : rate;
            rayTraceTasks.add(new RayTraceTask(images.get(i% imgCount), r, curRate));
        }
        try {
            List<Future<RayTraceTask>> futures = executorService.invokeAll(rayTraceTasks);
            ArrayList<Image> results = new ArrayList<>(imgCount);

            for (int i = 0; i < futures.size(); i++) {
                Future<RayTraceTask> f = futures.get(i);
                RayTraceTask task = f.get();
                Image tmpI = task.getImg();
                boolean add = true;
                for (int j = 0; j < results.size(); j++) {
                    if (tmpI == results.get(j)){
                        add = false;
                        break;
                    }
                }
                if (add) results.add(tmpI);
            }
            Image[] tmpRes = new Image[results.size()];
            results.toArray(tmpRes);
            return Image.mergeAVG(tmpRes);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
        return null;
    }
}

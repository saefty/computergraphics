package taher858897.a05.Threading.Executors;

import cgtools.Mat4;
import taher858897.Image;
import taher858897.a05.RayTracer.RayTracer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class RayTraceFragmentExcecutor {
    private ExecutorService executorService;
    List<Callable<RayTraceTaskFragment>> rayTraceTasks;

    public static int started = 0;
    public static int counter = 0;

    public static long startTime;

    public static int threadIds=0;

    class NamedThreadFactory implements ThreadFactory{
        @Override
        public Thread newThread(Runnable r) {
            threadIds++;
            Thread t = new Thread(r);
            t.setName("T-" + threadIds);
            return t;
        }
    }

    public RayTraceFragmentExcecutor(int threadCount){
        executorService = Executors.newFixedThreadPool(threadCount, new NamedThreadFactory());
        rayTraceTasks = new ArrayList<>();
    }

    public Image executeTracer(Image img, RayTracer r, int sampleRate, int xBlockSize, int yBlockSize){
        startTime = System.currentTimeMillis();
        int width = img.getWidth();
        int height = img.getHeight();
        int xIterations = width/xBlockSize;
        int yIterations = height/yBlockSize;


        for (int i = 0; i < xIterations; i++) {
            for (int j = 0; j < yIterations; j++) {
                int x_start = xBlockSize * i;
                int x_end = x_start + xBlockSize;
                int y_start = yBlockSize * j;
                int y_end = yBlockSize + y_start;
                rayTraceTasks.add(new RayTraceTaskFragment(img, r, sampleRate, x_start, x_end, y_start, y_end));
            }
        }
        if ((xIterations * xBlockSize != width)) {
            rayTraceTasks.add(new RayTraceTaskFragment(img, r, sampleRate, (xBlockSize * (xIterations -1)), width, 0, height));
        }
        if ((yIterations * yBlockSize != height)) {
            rayTraceTasks.add(new RayTraceTaskFragment(img, r, sampleRate, 0, width, (yBlockSize * (yIterations -1)), height));
        }
        started = rayTraceTasks.size();
        try {
            List<Future<RayTraceTaskFragment>> futures = executorService.invokeAll(rayTraceTasks);
            for (Future<RayTraceTaskFragment> future: futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return img;
        } catch (InterruptedException  e) {
            e.printStackTrace();
        }finally {
            executorService.shutdown();
        }
        return null;
    }
}

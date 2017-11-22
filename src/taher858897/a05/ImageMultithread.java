package taher858897.a05;

import taher858897.Image;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Sampler.StratifiedSampler;

import java.util.ArrayList;

public class ImageMultithread implements Runnable {
    private final Image i;
    private int id;
    private final Sampler sampler;
    private final ArrayList<Thread> threads;
    private final int x_start;
    private final int x_end;
    private final int y_start;
    private final int y_end;
    private long start_time;
    static int threads_count = -1;

    public ImageMultithread(int threads_count, Image i, Sampler s, int x_start, int x_end, int y_start, int y_end) {
        this.i = i;
        this.sampler = s;
        this.x_start = x_start;
        this.x_end = x_end;
        this.y_start = y_start;
        this.y_end = y_end;
        ImageMultithread.threads_count = threads_count;
        threads = new ArrayList<>();
    }

    public void startMultiThreading(){
        start_time = System.currentTimeMillis();
        double display_fac = Math.floor((x_end-x_start)/threads_count);
        for (int j = 1; j <= threads_count; j++) {
            int this_thread_start_x = (int) (display_fac * (j-1)) + x_start;
            int this_thread_end_x =(int) (display_fac * j) + x_start;
            System.out.println(j+ "  " + this_thread_start_x + " to "+ this_thread_end_x);
            ImageMultithread imageMultithread = new ImageMultithread(threads_count, i, sampler, this_thread_start_x, this_thread_end_x, y_start, y_end);
            imageMultithread.id = j;
            Thread t = new Thread(imageMultithread);
            t.start();
            threads.add(t);
        }
    }

    public void join(){
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Duration: " + (System.currentTimeMillis()-start_time)/1000.0 + "ms");

    }

    @Override
    public void run() {
        System.out.println(id + " start");
        i.sample(sampler, x_start, x_end, y_start, y_end);
        System.out.println(id + " done");
    }
}

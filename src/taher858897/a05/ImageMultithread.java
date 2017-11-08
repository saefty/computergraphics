package taher858897.a05;

import taher858897.Image;
import taher858897.a05.Sampler.Sampler;

import java.util.ArrayList;

public class ImageMultithread implements Runnable {
    private final Image i;
    private int id;
    private final Sampler sampler;
    private final ArrayList<Thread> threads;
    static int threads_count = -1;


    public ImageMultithread(Image i, Sampler s) {
        this.i = i;
        this.sampler = s;
        ImageMultithread.threads_count = 4;
        threads = new ArrayList<>();
    }

    public void startMultiThreading(){
        for (int j = 1; j <= threads_count; j++) {
            ImageMultithread imageMultithread = new ImageMultithread(i, sampler);
            imageMultithread.id = j;
            Thread t = new Thread(imageMultithread);
            t.start();
            threads.add(t);
        }
        for (Thread t: threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int div_factor = threads_count ;
        int x_start = i.getWidth()/div_factor * (id-1);
        int x_end = i.getWidth()/div_factor * id;
        int y_start = 0;
        int y_end = i.getHeight();
        i.sample(sampler, x_start, x_end, y_start, y_end);
        System.out.println(id + " done");
    }
}

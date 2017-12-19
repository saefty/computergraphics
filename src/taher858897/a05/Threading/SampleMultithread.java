package taher858897.a05.Threading;

import taher858897.Image;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Sampler.StratifiedSampler;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SampleMultithread implements Runnable{

    private final Sampler tracer;
    private final Image img;
    private final int SAMPLE_RATE;
    private final int threads_count;
    private final StratifiedSampler stratifiedSampler;

    private final ArrayList<Thread> threads;
    private final ArrayList<SampleMultithread> samplers;

    private final ExecutorService executorService;

    public SampleMultithread(Image img, Sampler r, int SAMPLE_RATE, int threads_count){
        tracer = r;
        this.img = img;
        this.SAMPLE_RATE = SAMPLE_RATE;
        this.threads_count = threads_count;
        this.stratifiedSampler = new StratifiedSampler(tracer, SAMPLE_RATE);
        this.threads = new ArrayList<>(threads_count);
        this.samplers = new ArrayList<>(threads_count);
        executorService = Executors.newFixedThreadPool(threads_count);
    }

    public void startMultiThreading(){
        int rate = SAMPLE_RATE / threads_count;
        for (int j = 1; j <= threads_count; j++) {
            SampleMultithread sampleMultithread = new SampleMultithread(new Image(img), tracer, rate, j);
            samplers.add(sampleMultithread);
            Thread t = new Thread(sampleMultithread);
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
    }

    private Image getImg() {
        return img;
    }

    public Image getImages() {
        Image[] images = new Image[samplers.size()];
        for (int i = 0, samplersSize = samplers.size(); i < samplersSize; i++) {
            SampleMultithread sampleMultithread = samplers.get(i);
            images[i] = sampleMultithread.getImg();
        }
        return Image.mergeAVG(images);
    }

    @Override
    public void run() {
        System.out.println(threads_count + " start");
        img.sample(stratifiedSampler);
        System.out.println(threads_count + " done");
    }
}

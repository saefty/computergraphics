package taher858897.a05.Threading.Executors;

import taher858897.Image;
import taher858897.a03.Ray;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Sampler.StratifiedSampler;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class RayTraceTask implements Callable{
    private final Sampler tracer;
    private final Image img;
    private final int SAMPLE_RATE;
    private final StratifiedSampler stratifiedSampler;

    public RayTraceTask(Image img, RayTracer r, int SAMPLE_RATE) {
        this.tracer = r;
        this.img = img;
        this.SAMPLE_RATE = SAMPLE_RATE;
        this.stratifiedSampler = new StratifiedSampler(tracer, SAMPLE_RATE);
    }
    public Image getImg() {
        return img;
    }

    @Override
    public Object call() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getId() + " start with " + SAMPLE_RATE);
        img.sample(stratifiedSampler);
        System.out.println(Thread.currentThread().getId()  + " done");
        RayTraceExcecutor.counter++;
        System.out.println(getStats(start));
        return this;
    }

    public String getStats(long thisStarted){
        StringBuilder s = new StringBuilder("");
        s.append((RayTraceExcecutor.counter/(double) RayTraceExcecutor.started)*100).append("% done in ");
        s.append((System.currentTimeMillis() - RayTraceExcecutor.startTime)/10000.0).append("s");
        s.append("LEFT: ").append(((System.currentTimeMillis() - thisStarted)*(RayTraceExcecutor.started-RayTraceExcecutor.counter))/10000.0).append("s ");
        return s.toString();

    }
}

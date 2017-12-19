package taher858897.a05.Threading.Executors;

import taher858897.Image;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Sampler.StratifiedSampler;

import java.util.concurrent.Callable;

public class RayTraceTaskFragment implements Callable{
    private final Sampler tracer;
    private final Image img;
    private final int x_start;
    private final int x_end;
    private final int y_start;
    private final int y_end;
    private final StratifiedSampler stratifiedSampler;

    public RayTraceTaskFragment(Image img, RayTracer r, int SAMPLE_RATE, int x_start, int x_end, int y_start, int y_end) {
        this.tracer = r;
        this.img = img;
        this.x_start = x_start;
        this.x_end = x_end;
        this.y_start = y_start;
        this.y_end = y_end;
        this.stratifiedSampler = new StratifiedSampler(tracer, SAMPLE_RATE);
    }

    public Image getImg() {
        return img;
    }

    @Override
    public Object call() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " start x:" + x_start +"-" + x_end + " y:" + y_start +"-" + y_end);
        img.sample(stratifiedSampler, x_start, x_end, y_start, y_end);
        System.out.println(Thread.currentThread().getName()  + " done");
        RayTraceFragmentExcecutor.counter++;
        System.out.println(getStats(start));
        return this;
    }

    public String getStats(long thisStarted){
        StringBuilder s = new StringBuilder("");
        s.append((RayTraceFragmentExcecutor.counter/(double) RayTraceFragmentExcecutor.started)*100).append("% done in ");
        s.append((System.currentTimeMillis() - RayTraceFragmentExcecutor.startTime)/10000.0).append("s");
        s.append("LEFT: ").append(((System.currentTimeMillis() - thisStarted)*(RayTraceFragmentExcecutor.started-RayTraceFragmentExcecutor.counter))/10000.0).append("s ");
        return s.toString();

    }
}

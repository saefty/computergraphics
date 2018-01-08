package taher858897.a05.Threading;

import taher858897.Image;
import taher858897.a05.Main;
import taher858897.a05.RayTracer.RayTracer;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Threading.Executors.RayTraceFragmentExcecutor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static taher858897.a05.Main.threads;
import static taher858897.a05.Main.xDim;
import static taher858897.a05.Main.yDim;


public class ImageMultithreadSocketReader {
    private static boolean busy = false;
    private static ServerSocket server;
    private static Socket socket;
    private static RayTraceFragmentExcecutor rayTraceFragmentExcecutor;

    public static void main(String[] args) throws IOException {
         server = new ServerSocket(9865);
            while (true) {
                try {
                    if (!busy){
                        socket = server.accept();
                        System.out.println("Accepted from:" + socket.getInetAddress());
                        if(rayTraceFragmentExcecutor == null ||rayTraceFragmentExcecutor != null && rayTraceFragmentExcecutor.getExecutorService().isShutdown()){
                           rayTraceFragmentExcecutor  = new RayTraceFragmentExcecutor(threads);
                        }
                    } else continue;
                    busy = true;

                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                    int imageW = (int) in.readInt();
                    int imageH = (int) in.readInt();
                    Image image = new Image(imageW, imageH);

                    RayTracer sampler = (RayTracer) in.readObject();
                    sampler.world.scene.loadTextures();

                    int SAMPLE_RATE = (int) in.readObject();
                    boolean SHADOWS =  in.readBoolean();
                    int xDim =  in.readInt();
                    int yDim =  in.readInt();
                    Main.xDim = xDim;
                    Main.yDim = yDim;
                    Main.SHADOWS = SHADOWS;

                    startMultiThreadRendering(image, sampler, SAMPLE_RATE);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    rayTraceFragmentExcecutor.shutdown();
                }
            }
    }

    public static void startMultiThreadRendering(Image image, Sampler s, int SAMPLE_RATE){
        image = rayTraceFragmentExcecutor.executeTracer(image, (RayTracer) s, SAMPLE_RATE, xDim, yDim);
        System.out.println("done");

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.print("Sending");
            out.writeObject(image);
            out.flush();
            socket.close();
            System.out.println("...done");
        } catch (Exception e) {
            e.printStackTrace();
            rayTraceFragmentExcecutor.shutdown();
        }
        busy = false;

    }
}

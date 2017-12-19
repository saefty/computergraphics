package taher858897.a05.Threading;

import taher858897.Image;
import taher858897.a05.Sampler.Sampler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class ImageMultithreadSocketReader {
    private static boolean busy = false;
    private static ServerSocket server;
    private static Socket socket;

    public static void main(String[] args) throws IOException {

            server = new ServerSocket(8770);
            while (true) {
                try {
                    if (!busy){
                        socket = server.accept();
                        System.out.println("Accepted from:" + socket.getInetAddress());
                    } else continue;
                    busy = true;

                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    int imageW = (int) in.readObject();
                    int imageH = (int) in.readObject();
                    Image image = new Image(imageW, imageH);

                    Sampler sampler = (Sampler) in.readObject();

                    int SAMPLE_RATE = (int) in.readObject();

                    System.out.println("done");

                    startMultiThreadRendering(image, sampler, SAMPLE_RATE);

                } catch (IOException | ClassNotFoundException ignored) {
                    ignored.printStackTrace();
                }
            }


    }

    public static void startMultiThreadRendering(Image image, Sampler s, int SAMPLE_RATE){
        SampleMultithread sampleMultithread = new SampleMultithread(image, s, SAMPLE_RATE, 8);
        sampleMultithread.startMultiThreading();
        sampleMultithread.join();
        System.out.println("done");
        System.out.println("Start merging images");
        image = sampleMultithread.getImages();
        System.out.println("Done merging images");

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            System.out.print("Sending");
            out.writeObject(image);
            out.flush();
            socket.close();
            System.out.println("...done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        busy = false;

    }
}

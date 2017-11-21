package taher858897.a05;

import taher858897.Image;
import taher858897.a05.ImageMultithread;
import taher858897.a05.Sampler.Sampler;
import taher858897.a05.Shape.Group;

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

                    int x_start = (int) in.readObject();
                    int x_end = (int) in.readObject();
                    int y_start = (int) in.readObject();
                    int y_end = (int) in.readObject();

                    System.out.println("done");

                    startMultiThreadRendering(image, sampler, x_start, x_end, y_start, y_end);

                } catch (IOException | ClassNotFoundException ignored) {
                    ignored.printStackTrace();
                }
            }


    }

    public static void startMultiThreadRendering(Image image, Sampler s, int x_start, int x_end, int y_start, int y_end){
        image.initLocks();
        ImageMultithread imageMultithread = new ImageMultithread(16, image, s, x_start, x_end, y_start, y_end);
        imageMultithread.startMultiThreading();
        imageMultithread.join();
        System.out.println("done");

        /*String filename = "doc/a061-scene.png";
        try {
            System.out.println("Start writing image: " + filename);

            image.write(filename);
            System.out.println("Wrote image: " + filename);
        } catch (IOException error) {
            System.out.println(String.format("Something went wrong writing: %s: %s", filename, error));
        }*/

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

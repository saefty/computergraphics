package taher858897.a05;

import taher858897.Image;
import taher858897.a05.Sampler.Sampler;

import java.io.*;
import java.net.Socket;

public class ImageMultithreadSocketWriter implements Runnable{
    final Socket s;
    final ObjectOutputStream out;
    public Image RESULT_IMG;
    final Image image;
    final Sampler sampler;
    final int x_start;
    final int x_end;
    final int y_start;
    final int y_end;

    public ImageMultithreadSocketWriter(String host, int port, Image image, Sampler sampler, int x_start, int x_end, int y_start, int y_end) throws IOException {
        this.image = image;
        this.sampler = sampler;
        this.x_start = x_start;
        this.x_end = x_end;
        this.y_start = y_start;
        this.y_end = y_end;
        this.s =  new Socket(host, port);
        //BufferedOutputStream bout = new BufferedOutputStream(s.getOutputStream());
        this.out = new ObjectOutputStream(s.getOutputStream());
    }

    private Image sendDataWaitForResponse(Image image, Sampler s, int x_start, int x_end, int y_start, int y_end) throws IOException {
        out.writeObject(image.getWidth());
        out.writeObject(image.getHeight());
        out.flush();
        out.writeObject(s);
        out.flush();
        out.writeObject(x_start);
        out.writeObject(x_end);
        out.writeObject(y_start);
        out.writeObject(y_end);
        out.flush();
        ObjectInputStream in = new ObjectInputStream(this.s.getInputStream());
        try {
            System.out.println("Wait on response");
            Image newImg = (Image) in.readObject();
            newImg.initLocks();
            this.s.close();
            System.out.println("Socket closed.");
            return newImg;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        try {
            RESULT_IMG = sendDataWaitForResponse(image, sampler, x_start,x_end,y_start,y_end);
            RESULT_IMG.initLocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

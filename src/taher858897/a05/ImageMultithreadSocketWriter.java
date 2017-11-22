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
    final int SAMPLE_RATE;


    public ImageMultithreadSocketWriter(String host, int port, Image image, Sampler sampler, int SAMPLE_RATE) throws IOException {
        this.image = image;
        this.sampler = sampler;
        this.SAMPLE_RATE = SAMPLE_RATE;

        this.s =  new Socket(host, port);
        //BufferedOutputStream bout = new BufferedOutputStream(s.getOutputStream());
        this.out = new ObjectOutputStream(s.getOutputStream());
    }

    private Image sendDataWaitForResponse(Image image, Sampler s, int SAMPLE_RATE) throws IOException {
        out.writeObject(image.getWidth());
        out.writeObject(image.getHeight());
        out.flush();
        out.writeObject(s);
        out.flush();
        out.writeObject(SAMPLE_RATE);
        out.flush();
        ObjectInputStream in = new ObjectInputStream(this.s.getInputStream());
        try {
            System.out.println("Wait on response");
            Image newImg = (Image) in.readObject();
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
            RESULT_IMG = sendDataWaitForResponse(image, sampler, SAMPLE_RATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

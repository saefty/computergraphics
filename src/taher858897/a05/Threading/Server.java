package taher858897.a05.Threading;

public class Server {
    public final String server;
    public final int sampleRate;
    public final int port;

    public Server(String server,int port, int sampleRate) {
        this.server = server;
        this.port = port;
        this.sampleRate = sampleRate;
    }
}

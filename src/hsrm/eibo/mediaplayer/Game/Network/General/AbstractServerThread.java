package hsrm.eibo.mediaplayer.Game.Network.General;

public abstract class AbstractServerThread extends Thread {
    public static final int SO_TIMEOUT = 1000;
    protected int serverPort;

    public AbstractServerThread(String threadName, int port) {
        super(threadName);
        this.setDaemon(true);
        this.serverPort = port;
    }
}

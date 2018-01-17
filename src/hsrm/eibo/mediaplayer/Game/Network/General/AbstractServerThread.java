package hsrm.eibo.mediaplayer.Game.Network.General;

public abstract class AbstractServerThread extends Thread {
    protected int serverPort;

    public AbstractServerThread(String threadName, int port) {
        super(threadName);
        this.setDaemon(true);
        this.serverPort = port;
    }
}

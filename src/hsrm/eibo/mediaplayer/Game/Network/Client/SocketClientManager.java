package hsrm.eibo.mediaplayer.Game.Network.Client;

import hsrm.eibo.mediaplayer.Game.Network.Client.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketClientManager extends AbstractSocketManager {

    private P2pClientThread clientThread;
    private Thread serverThread;
    private InetAddress localhost, serverAddress;

    public SocketClientManager(InetAddress serverAddress) {
        try {
            this.serverAddress = serverAddress;
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void startClient()
    {
        clientThread = new P2pClientThread(this.serverAddress, APPLICATION_PORT);
        clientThread.start();

        // initiate connection start..
        clientThread.pushToProcessingQueue("hello;User with Toaster");
    }

    public void close() {
        clientThread.interrupt();
    }
}
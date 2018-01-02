package hsrm.eibo.mediaplayer.Game.Network;

import hsrm.eibo.mediaplayer.Game.Network.Thread.Host.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.Thread.Host.P2pServerThread;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketHostManager extends SocketManager {

    private Thread clientThread, serverThread;
    private InetAddress localhost;

    public SocketHostManager() {
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void startServerThread() {
        serverThread = new P2pServerThread(APPLICATION_PORT);
        serverThread.start();
    }

    public void startClient()
    {
        clientThread = new P2pClientThread(localhost, APPLICATION_PORT);
    }

}
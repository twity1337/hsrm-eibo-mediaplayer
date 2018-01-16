package hsrm.eibo.mediaplayer.Game.Network.Host;

import hsrm.eibo.mediaplayer.Game.Network.Host.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.Host.Thread.P2pServerThread;
import hsrm.eibo.mediaplayer.Game.Network.SocketManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SocketHostManager extends SocketManager {

    private Thread clientThread, serverThread;
    private InetAddress localhost;

    private ArrayList<InetAddress> connectedClientAdresses = new ArrayList<>();

    public SocketHostManager() {
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void addConnectedClient(InetAddress adress) {
        this.connectedClientAdresses.add(adress);
    }

    public void removeConnectedClient(InetAddress address) {
        this.connectedClientAdresses.remove(address);
    }

    public void startP2pServerThread() {
        serverThread = new P2pServerThread(APPLICATION_PORT);
        serverThread.start();
        System.out.println("Server started at: " + localhost.toString() + " on port " + APPLICATION_PORT + "....");
    }

    public void startP2pClientThread()
    {
        clientThread = new P2pClientThread(localhost, APPLICATION_PORT);
    }

}
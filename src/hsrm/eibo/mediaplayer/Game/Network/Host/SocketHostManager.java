package hsrm.eibo.mediaplayer.Game.Network.Host;

import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;
import hsrm.eibo.mediaplayer.Game.Network.General.ObservableConnectedClientList;
import hsrm.eibo.mediaplayer.Game.Network.Host.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.Host.Thread.P2pServerThread;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketHostManager extends AbstractSocketManager{

    private Thread clientThread, serverThread;
    private InetAddress localhost;


    private ObservableConnectedClientList connectedClients = new ObservableConnectedClientList();

    /**
     * Singleton instance
     */
    private static SocketHostManager instance = new SocketHostManager();

    /**
     * A boolean, indicating if a server is running.
     */
    private boolean running = false;

    public static SocketHostManager getInstance() {
        return instance;
    }

    private SocketHostManager() {
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter for running field.
     *
     * @return
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Getter for connected client band members
     * @return
     */
    public ObservableConnectedClientList getConnectedClientList() {
        return connectedClients;
    }

    /**
     * Starts the peer to peer server.
     */
    public void startP2pServerThread() {
        running = true;
        serverThread = new P2pServerThread(APPLICATION_PORT);
        serverThread.start();
    }

    public void startP2pClientThread() {
        running = true;
        clientThread = new P2pClientThread(localhost, APPLICATION_PORT);
    }

    public void close() {
        if (serverThread != null)
            serverThread.interrupt();
        if (clientThread != null)
            clientThread.interrupt();
    }
}
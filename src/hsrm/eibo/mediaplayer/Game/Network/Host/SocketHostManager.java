package hsrm.eibo.mediaplayer.Game.Network.Host;

import hsrm.eibo.mediaplayer.Game.Network.Host.Thread.P2pClientThread;
import hsrm.eibo.mediaplayer.Game.Network.Host.Thread.P2pServerThread;
import hsrm.eibo.mediaplayer.Game.Network.General.AbstractSocketManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class SocketHostManager extends AbstractSocketManager {

    private Thread clientThread, serverThread;
    private InetAddress localhost;


    /**
     * An array list of connected clients for broadcast messages.
     */
    private ArrayList<InetAddress> connectedClientAdresses = new ArrayList<>();

    /**
     * Singleton instance
     */
    private static SocketHostManager instance = new SocketHostManager();
    public static SocketHostManager getInstance() {return instance;}

    private SocketHostManager() {
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a new connected client.
     * @param adress The IP adress of the client stored as InetAddress object.
     */
    public void addConnectedClient(InetAddress adress) {
        this.connectedClientAdresses.add(adress);
    }

    /**
     * Removes / Disconnects a connected client.
     * The given client will no longer get network messages.
     * @param address The IP adress of the client stored as InetAddress object.
     */
    public void removeConnectedClient(InetAddress address) {
        this.connectedClientAdresses.remove(address);
    }

    /**
     * Returns all connected clients as an array list.
     * @return
     */
    public ArrayList<InetAddress> getConnectedClientAdresses() {
        return connectedClientAdresses;
    }

    public void startP2pServerThread() {
        serverThread = new P2pServerThread(APPLICATION_PORT);
        serverThread.start();
    }

    public void startP2pClientThread()
    {
        clientThread = new P2pClientThread(localhost, APPLICATION_PORT);
    }
}
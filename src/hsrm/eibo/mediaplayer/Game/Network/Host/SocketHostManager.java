package hsrm.eibo.mediaplayer.Game.Network.Host;

import hsrm.eibo.mediaplayer.Game.Model.BandMember;
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
    private ArrayList<BandMember> connectedClients = new ArrayList<>();

    /**
     * All connected client InetAddress-objects
     */
    private InetAddress[] connectedClientAddresses = null;

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
     * Adds a new connected client.
     *
     * @param client The client to add.
     */
    public void addConnectedClient(BandMember client) {
        this.connectedClientAddresses = null; // invalidate cached connectedClients on change
        this.connectedClients.add(client);
    }

    /**
     * Removes / Disconnects a connected client.
     * The given client will no longer get network messages.
     *
     * @param client The client to remove.
     */
    public void removeConnectedClient(BandMember client) {
        this.connectedClientAddresses = null; // invalidate cached connectedClients on change
        this.connectedClients.remove(client);
    }

    /**
     * Getter for connected client band members
     * @return
     */
    public ArrayList<BandMember> getConnectedClients() {
        return connectedClients;
    }

    /**
     * Returns all connected client addresses as an array list.
     *
     * @return
     */
    public synchronized InetAddress[] getConnectedClientAddresses() {
        // check if cached connected clients are invalidated..
        if (this.connectedClientAddresses == null) {
            InetAddress[] addresses = new InetAddress[this.connectedClients.size()];
            for (int i = 0; i <= this.connectedClients.size(); i++) {
                addresses[i] = this.connectedClients.get(i).getClientAddress();
            }
            this.connectedClientAddresses = addresses;
        }
        return this.connectedClientAddresses;
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